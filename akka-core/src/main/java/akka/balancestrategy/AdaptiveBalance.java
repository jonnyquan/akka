package akka.balancestrategy;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.cluster.metrics.NodeMetrics;
import akka.cluster.metrics.StandardMetrics;
import akka.enums.RouterGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by ruancl@xkeshi.com on 2016/12/23.
 * 根据各服务器状态 来平衡
 * 方案1(检测统计各个服务器响应时间,对服务器进行打分,再使用加权 进行离散随机)
 */
public class AdaptiveBalance extends AbstractLoadBalance {

    private Random random = new Random();

    private ConcurrentHashMap<Address,Integer> addrScore = new ConcurrentHashMap<>();

    @Override
    public boolean matchRouterGroup(RouterGroup routerGroup) {
        return routerGroup == RouterGroup.BALANCE;
    }

    @Override
    public boolean needListenAddr() {
        return false;
    }

    @Override
    public boolean needListenStatus() {
        return true;
    }

    @Override
    public void updateAddr(Map<Address, ActorRef> map) {

    }


    @Override
    public void updateServerStatu(Iterable<NodeMetrics> nodeMetrics) {
        Map<Address,Long> useHeap = new HashMap<>();
        Long totalHeap = 0l;
        for(NodeMetrics nodeMetric : nodeMetrics){
            Address address = nodeMetric.address();
            //根据内存占用率进行分配
            StandardMetrics.HeapMemory heap = StandardMetrics.extractHeapMemory(nodeMetric);
            Long usedHeap = heap.used();
            useHeap.put(address,usedHeap);
            totalHeap += usedHeap;
        }
        //打分 // TODO: 2016/12/23 分数打反了  均衡算法需要调整
        for(Address addr : useHeap.keySet()){
            long heap = useHeap.get(addr);
            int sc = (int)(heap*100/totalHeap);
            addrScore.put(addr,sc);
        }
    }

    @Override
    protected ActorRef needListenStrategy() {
        return null;
    }

    @Override
    protected ActorRef notNeedListenStrategy(Map<Address,ActorRef> actorRefs) {
        List<Map.Entry<Address,ActorRef>> entryList = actorRefs.entrySet().stream().collect(Collectors.toList());
        List<Map.Entry<Address,Integer>> entryListScore = addrScore.entrySet().stream().collect(Collectors.toList());
        int size = entryListScore.size();
        int trueSize = entryList.size();

        /**
         * 服务器状态还未初始化  或者 实际有服务器掉线,状态还未更新  都直接返回第一台服务器
         */
        if(size==0){
            return entryList.get(0).getValue();
        }

        int[] scores = new int[size];
        int coreCount = 0;

        //获取服务器状态
        //*********************************
        for(int i=0 ;i<size;i++){
            int score = entryListScore.get(i).getValue();
            scores[i] = score;
            coreCount += scores[i];
        }
        //***************************
        int[] randomScore = new int[size];

        int randomInt = random.nextInt(coreCount);
        for(int i=0 ;i<size;i++){
            int s;
            int last = 0;
            if(i > 0){
                last = randomScore[i-1];
            }
            s = scores[i] + last;

            if(randomInt<s && randomInt>=last){
                if(i>=trueSize){
                    return entryList.get(0).getValue();
                }
                return entryList.get(i).getValue();
            }
            randomScore[i] = s;
        }

        throw new IllegalAccessError("系统异常");
    }
}
