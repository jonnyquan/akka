package akka.core;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actors.ClusterListener;
import akka.addrstrategy.ClusterAddress;
import akka.balancestrategy.ServerStatus;
import akka.cluster.Cluster;
import akka.msg.Constant;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * Created by ruancl@xkeshi.com on 2016/12/22.
 */
public abstract class AbstractAkkaSystem implements Akka {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAkkaSystem.class);

    private final ActorSystem system;

    private AddressStrategy addressStrategy;

    private String systemName;

    /**
     */
    public AbstractAkkaSystem(String systemName) {
        this.systemName = systemName;
        Config config = ConfigFactory.load();
        this.system  = ActorSystem.create(systemName, config);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        //在节点监听还未成功建立前阻塞消息
        Cluster.get(system).registerOnMemberUp(() -> countDownLatch.countDown());
        if (countDownLatch.getCount() == 1) {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("actor system创建完毕");
        ClusterAddress clusterAddress = null;
        ServerStatus serverStatus = null;
       // RouteesAddress routeesAddress = null;
        if (Constant.WITH_CLUSTER) {
            clusterAddress = new ClusterAddress(system);
            serverStatus = new ServerStatus();
            system.actorOf(Props.create(ClusterListener.class,clusterAddress,serverStatus));
        }
       /* if (Constant.WITH_ROUTER){
            routeesAddress = new RouteesAddress(system);
        }*/
        addressStrategy = new AddressStrategy(clusterAddress,serverStatus);
        logger.info("actor system 扩展功能启动完毕");
    }

    protected AddressStrategy getAddressStrategy() {
        return addressStrategy;
    }

    protected String getSystemName() {
        return systemName;
    }

    protected ActorSystem getSystem() {
        return system;
    }



}
