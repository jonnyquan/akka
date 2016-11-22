package akka.msg;

import java.io.Serializable;

/**
 * Created by ruancl@xkeshi.com on 16/10/11.
 */
public class RpcResult implements Serializable {

    private Object o;

    private int status = 0;

    public RpcResult(Object o) {
        this.o = o;
    }

    public Object getO() {
        return o;
    }
}
