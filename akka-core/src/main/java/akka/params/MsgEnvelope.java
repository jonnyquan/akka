package akka.params;

/**
 * Created by ruancl@xkeshi.com on 16/10/17.
 */
public class MsgEnvelope {

    public final String topic;

    public final Object payload;

    public MsgEnvelope(String topic, Object payload) {
        this.topic = topic;
        this.payload = payload;
    }
}
