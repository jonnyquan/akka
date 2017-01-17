package akka.params;

import akka.actors.BaseActor;
import akka.actors.BaseService;
import akka.routing.Pool;
import akka.routing.RoundRobinPool;

/**
 * Created by ruancl@xkeshi.com on 16/10/20.
 * actor注册包装类
 */
public class RegisterWrapper {

    private final Class tClass = BaseActor.class;

    private final String name;

    private Pool pool = new RoundRobinPool(1);

    private BaseService ref;



    public RegisterWrapper(String name, Pool pool, BaseService baseService) {
        this.name = name;
        this.pool = pool;
        this.ref = baseService;
    }

    public BaseService getRef() {
        return ref;
    }

    public Class gettClass() {
        return tClass;
    }

    public String getName() {
        return name;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }
}
