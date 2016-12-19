package akka.core;

import akka.actor.ActorRef;
import akka.event.japi.SubchannelEventBus;
import akka.param.MsgEnvelope;
import akka.util.Subclassification;

/**
 * Created by ruancl@xkeshi.com on 16/10/17.
 * <p>
 * 通过字符串匹配主题   subclassification可以调整匹配字符串规则
 */
public class SubChannelBus extends SubchannelEventBus<MsgEnvelope, ActorRef, String> {
    @Override
    public Subclassification<String> subclassification() {
        return new Subclassification<String>() {
            @Override
            public boolean isEqual(String s, String k1) {
                return s.equals(k1);
            }

            @Override
            public boolean isSubclass(String s, String k1) {
                return s.startsWith(k1);
            }
        };
    }

    @Override
    public String classify(MsgEnvelope msgEnvelope) {
        return msgEnvelope.topic;
    }

    @Override
    public void publish(MsgEnvelope msgEnvelope, ActorRef actorRef) {
        actorRef.tell(msgEnvelope.payload, ActorRef.noSender());
    }
}
