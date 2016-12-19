package test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ruancl@xkeshi.com on 16/11/16.
 * 需要spring支持
 */
public class MainSpring {
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

    }
}
