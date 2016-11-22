package test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ruancl@xkeshi.com on 16/11/16.
 */
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        TestService testService = context.getBean(TestService.class);

        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("发送消息" + i);
            testService.testMsg();
        }
       /* // 初始化Actor系统，系统名称必须和Server端一样
        ActorSystem system = ActorSystem.create(Constant.SYSTEM_NAME,ConfigFactory.load());
        // 需要调用的服务器端的Actor地址，多个地址将同时发消息给多个
        Iterable<String> routeesPaths = Collections.singletonList("/user/test2");
        // 通过ClusterRouterGroup获取服务器指定角色的Actor（必须在当前节点注册完成前调用）
        final ActorRef clientActor = system.actorOf(new ClusterRouterGroup(
                new AdaptiveLoadBalancingGroup(HeapMetricsSelector.getInstance(), Collections.<String>emptyList()),
                new ClusterRouterGroupSettings(100, routeesPaths, false, Constant.ROLE_NAME)
        ).props(), "clientActor");
        // 必须在当前节点注册加入集群并状态为up后才能发送消息
        Cluster.get(system).registerOnMemberUp(new Runnable() {
            public void run() {
                // 循环10次发送消息到ServerActor1中
                for(int i=0; i< 10; i++)
                    clientActor.tell(new Message("hello"), ActorRef.noSender());
            }
        });*/
    }
}
