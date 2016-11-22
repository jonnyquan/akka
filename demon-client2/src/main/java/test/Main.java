package test;

import akka.test.Hello;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ruancl@xkeshi.com on 16/11/16.
 */
public class Main {
    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        Hello hello = context.getBean(Hello.class);
        System.out.println(hello.sayHello());
    }
}
