package test;

import akka.actor.ActorRef;
import akka.core.Akka;
import akka.core.Sender;
import akka.enums.RouterGroup;
import akka.main.AkkaMain;
import akka.msg.Message;
import akka.params.AskProcessHandler;
import akka.params.AskProcessHandlerAdapt;
import akka.params.CutParam;
import akka.pattern.AskTimeoutException;

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
        Sender ask = akka.createAskSender("test", new AskProcessHandlerAdapt<Message,Message>() {
            @Override
            public void onSuccess(ActorRef actorRef, Object o) {
                System.out.println(actorRef.path() + ":成功接收:-----------object:" + ((Message) o).getContent());
            }

            @Override
            public void onFailure(ActorRef actorRef, Throwable throwable, AskProcessHandler<Message, Message> askProcessHandler, CutParam cutParam) {
                System.out.println("failure------------------" + throwable);
                if (throwable instanceof AskTimeoutException) {
                    System.out.println(actorRef.path() + ":链接超时 ");
                }
                //同时记录失败信息
            }

            @Override
            public void onComplete(ActorRef actorRef, Throwable throwable, Object o) {
                System.out.println("结束");
            }
        }, RouterGroup.RANDOM);


        for (int i = 0; i < 10; i++) {
            System.out.println("发送消息" + i);
            ask.sendMsg(new Message("hello__ask"));
        }
    }
}
