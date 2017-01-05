package transaction;

import akka.AkkaProcessHandler;
import akka.actor.ActorRef;
import akka.cluster.routing.ClusterRouterGroup;
import akka.cluster.routing.ClusterRouterGroupSettings;
import akka.msg.Constant;
import akka.params.RegisterBean;
import akka.routing.RandomGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

/**
 * Created by ruancl@xkeshi.com on 2016/12/29.
 */
@Component
public class TransactionService {

    @Autowired
    private AkkaProcessHandler akkaProcessHandler;

    public void needDisTransaction(int tid) {
        System.out.println("被系统A调用");
        Iterable routeesPaths = Arrays.asList(String.format("/user/F%s", tid));

        ClusterRouterGroup clusterRouterGroup = new ClusterRouterGroup(new RandomGroup(routeesPaths),
                new ClusterRouterGroupSettings(100, routeesPaths,
                        true, Constant.ROLE_NAME));
        ActorRef father = akkaProcessHandler.getAkka().registerRouterActor(clusterRouterGroup);
        try {
            Thread.sleep(1000l);
            System.out.println("执行业务B");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CountDownLatch countDownLatch = new CountDownLatch(1);
        akkaProcessHandler.getAkka().register(new RegisterBean(ServerActor.class, "son" + tid, father, new Transacation() {
            @Override
            public void commit() {
                System.out.println("over");
                countDownLatch.countDown();
            }

            @Override
            public void rollback() {
                System.out.println("rollback");
                countDownLatch.countDown();
            }
        }));
        if (countDownLatch.getCount() > 0) {
            try {
                countDownLatch.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
