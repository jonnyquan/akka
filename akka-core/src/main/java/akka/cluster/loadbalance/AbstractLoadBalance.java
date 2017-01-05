package akka.cluster.loadbalance;

import akka.actor.Address;
import akka.cluster.LoadBalance;

import java.util.Set;

/**
 * Created by ruancl@xkeshi.com on 2016/12/22.
 */
public abstract class AbstractLoadBalance implements LoadBalance {


    /**
     * @return
     * @needListen true 实现该接口
     */
    protected abstract Address needListenStrategy();

    /**
     * @return
     * @needListen false 实现该接口
     */
    protected abstract Address notNeedListenStrategy(Set<Address> actorRefMap);

    @Override
    public Address router(Set<Address> actorRefMap) {
        if (!needListenAddr() && !needListenStatus()) {
            return notNeedListenStrategy(actorRefMap);
        } else {
            return needListenStrategy();
        }
    }


    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass();
    }
}
