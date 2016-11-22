package akka.params;

/**
 * Created by ruancl@xkeshi.com on 16/11/9.
 */
public class CutParam<T> {

    private T msg;

    public CutParam(T msg) {
        this.msg = msg;
    }

    public T getMsg() {
        return msg;
    }

    public void setMsg(T msg) {
        this.msg = msg;
    }
}
