package akka.msg;

import akka.cluster.ClusterInterface;
import akka.cluster.addrs.ActorRefAddr;

/**
 * Created by ruancl@xkeshi.com on 16/10/26.
 */
public class Constant {


    public final static String ROLE_NAME = "customRoleName";

    public final static String SYSTEM_NAME = "EsbSystem";

    public final static Class<? extends ClusterInterface> CLUSTER_STRATEGY = ActorRefAddr.class;

}
