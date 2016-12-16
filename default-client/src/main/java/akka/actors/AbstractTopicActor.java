package akka.actors;

import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * Created by ruancl@xkeshi.com on 16/10/17.
 * 以主题为单位的消息广播需要继承该接口
 */
public abstract class AbstractTopicActor extends AbstractActor {

    private final String topic;

    public AbstractTopicActor(String topic) {
        this.topic = topic;
    }

    @Override
    public void aroundReceive(PartialFunction<Object, BoxedUnit> receive, Object msg) {
        if (msg instanceof String && msg.equals("are you ready?")) {
            getSender().tell(topic, getSelf());
        }
        super.aroundReceive(receive, msg);
    }
}
