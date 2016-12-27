package akka.params;

/**
 * Created by ruancl@xkeshi.com on 16/11/9.
 * 多消息ask 集中处理时候 入参格式
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
