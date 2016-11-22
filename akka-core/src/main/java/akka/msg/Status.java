package akka.msg;

import java.io.Serializable;

/**
 * Created by ruancl@xkeshi.com on 16/10/11.
 */
public class Status implements Serializable {

    private String msg;

    public Status(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
