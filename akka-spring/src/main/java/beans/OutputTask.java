package beans;

/**
 * Created by ruancl@xkeshi.com on 2017/1/6.
 */
public class OutputTask {

    private Long id;

    private Integer uid;

    private Integer size;

    public OutputTask(Long id, Integer uid, Integer size) {
        this.id = id;
        this.uid = uid;
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public Integer getUid() {
        return uid;
    }

    public Integer getSize() {
        return size;
    }
}
