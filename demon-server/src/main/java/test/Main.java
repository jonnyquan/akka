package test;

import akka.enter.AkkaInitFactory;
import akka.enter.MsgSender;
import akka.enums.RequestType;
import akka.enums.TransferType;
import akka.msg.Message;

/**
 * Created by ruancl@xkeshi.com on 2016/12/15.
 * 不依赖spring容器
 */
public class Main {

    public static void main(String[] args) {
        AkkaInitFactory akkaInitFactory = new AkkaInitFactory();

        MsgSender sender = akkaInitFactory.createMsgGun("test2");
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("发送消息" + i);
            sender.sendMsg(new Message("tell 路由"), RequestType.TELL, TransferType.ROUTER);
        }
    }
}
