package test;

import akka.core.Akka;
import akka.core.Sender;
import akka.main.AkkaMain;
import akka.msg.Message;

/**
 * Created by ruancl@xkeshi.com on 2016/12/15.
 * 不依赖spring容器
 */
public class Main {

    public static void main(String[] args) {
        Akka akka = AkkaMain.initAkka();
        Sender sender = akka.createTellSender("test2");

        for (int i = 0; i < 10; i++) {
            System.out.println("发送消息" + i);
            sender.sendMsg(new Message("tell 路由"));
        }
    }
}
