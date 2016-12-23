package akka.balancestrategy;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.enums.RouterGroup;

import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by ruancl@xkeshi.com on 2016/12/23.
 * 根据各服务器状态 来平衡
 * 方案1(检测统计各个服务器响应时间,对服务器进行打分,再使用加权 进行离散随机)
 */
public class AdaptiveBalance extends AbstractLoadBalance {

    private Random random = new Random();

    @Override
    public boolean matchRouterGroup(RouterGroup routerGroup) {
        return routerGroup == RouterGroup.BALANCE;
    }

    @Override
    public boolean needListen() {
        return false;
    }

    @Override
    public void update(Map<Address, ActorRef> map) {

    }

    @Override
    protected ActorRef needListenStrategy() {
        return null;
    }

    @Override
    protected ActorRef notNeedListenStrategy(Map<Address,ActorRef> actorRefs) {
        final int size = actorRefs.size();
        int[] scores = new int[size];
        int coreCount = 0;

        //获取服务器状态  暂时默认 权重都为1
        //*********************************
        for(int i=0 ;i<size;i++){
            scores[i] = 1;
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
                return actorRefs.values().stream().collect(Collectors.toList()).get(i);
            }
            randomScore[i] = s;
        }

        throw new IllegalAccessError("系统异常");
    }
}
