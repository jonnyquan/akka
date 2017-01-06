package akka.msg;

import akka.cluster.ClusterContext;
import akka.cluster.addrs.RouteesAddress;

/**
 * Created by ruancl@xkeshi.com on 16/10/26.
 */
public class Constant {


    public final static String ROLE_NAME = "customRoleName";

    public final static String SYSTEM_NAME = "EsbSystem";

    public final static Class<? extends ClusterContext> CLUSTER_STRATEGY = RouteesAddress.class;

}
