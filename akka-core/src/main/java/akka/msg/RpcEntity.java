package akka.msg;

import java.io.Serializable;

/**
 * Created by ruancl@xkeshi.com on 16/10/11.
 */
public class RpcEntity implements Serializable {

    private Class interfaceName;

    private String method;

    private Object[] param;

    public RpcEntity(Class interfaceName, String method, Object[] param) {
        this.interfaceName = interfaceName;
        this.method = method;
        this.param = param;
    }


    public Class getInterfaceName() {
        return interfaceName;
    }

    public String getMethod() {
        return method;
    }

    public Object[] getParam() {
        return param;
    }
}
