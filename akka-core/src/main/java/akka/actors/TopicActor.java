package akka.actors;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.enter.SubChannelBus;


/**
 * Created by ruancl@xkeshi.com on 16/10/17.
 */
public class TopicActor extends UntypedActor {

    private SubChannelBus eventBus;

    public TopicActor(SubChannelBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void onReceive(Object o) throws Throwable {
        System.out.println("收到消息:" + o + "地址:" + getSender().path().address());
        String add = getSender().path().address().toString();
        if (o instanceof String[]) {
            String[] arr = (String[]) o;
            for (String path : arr) {
                //与客户家做一次握手确认该actor状态可用
                ActorSelection selection = getContext().actorSelection(add + path);
                selection.tell("are you ready?", getSelf());
            }
            getSender().tell("主题注册成功", null);
        } else if (o instanceof String) {
            eventBus.subscribe(getSender(), o.toString());
        }
    }
}
