package transaction;

/**
 * Created by ruancl@xkeshi.com on 2016/12/29.
 */
public interface Transacation {

    void commit();

    void rollback();
}
