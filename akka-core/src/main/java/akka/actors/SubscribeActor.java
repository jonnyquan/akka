package akka.actors;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.msg.Constant;

import java.util.List;

/**
 * Created by ruancl@xkeshi.com on 16/10/17.
 */
public class SubscribeActor extends UntypedActor {

    private final String path = "/user/" + Constant.TOPIC_PATH;

    public SubscribeActor(final List<String> list, final String mainPath) {
        ActorSelection selection =
                getContext().actorSelection(mainPath + path);
        System.out.println("发送主题注册");
        selection.tell(list.toArray(new String[list.size()]), getSelf());
    }

    @Override
    public void onReceive(Object o) throws Throwable {
        System.out.println("收到消息:" + o);
    }
}
