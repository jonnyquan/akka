package test;

import akka.core.AkkaSystem;
import akka.enums.RouterStrategy;
import akka.main.AkkaMain;
import akka.core.MsgSender;
import akka.enums.RequestType;
import akka.enums.TransferType;
import akka.msg.Message;

/**
 * Created by ruancl@xkeshi.com on 2016/12/15.
 * 不依赖spring容器
 */
public class Main {

    public static void main(String[] args) {
        AkkaSystem akkaSystem = AkkaMain.InitAkkaSystem();
        MsgSender sender = akkaSystem.createMsgGun("test2");
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("发送消息" + i);
            sender.sendMsg(new Message("tell 路由"), RequestType.TELL, RouterStrategy.RANDOM);
        }
    }
}
