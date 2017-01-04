package test;

import akka.core.Akka;
import akka.core.Sender;
import akka.enums.RouterGroup;
import akka.main.AkkaMain;
import akka.msg.Message;

/**
 * Created by ruancl@xkeshi.com on 2016/12/15.
 * 不依赖spring容器
 */
public class Main {

    public static void main(String[] args) {
        Akka akka = AkkaMain.initAkka();
        //单工消息
        Sender tell = akka.createTellSender("test2");
        //双攻
        Sender ask = akka.createAskSender("test",new CustomAskProcessHandler(), RouterGroup.RANDOM);
        for (int i = 0; i < 10; i++) {
            System.out.println("发送消息" + i);
            ask.sendMsg(new Message("hello__ask"));
        }
    }
}
