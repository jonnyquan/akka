package test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by ruancl@xkeshi.com on 16/11/16.
 * 需要spring支持
 */
public class MainWithSpring {
    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
    }
}
