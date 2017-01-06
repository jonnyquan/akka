package beans;

/**
 * Created by ruancl@xkeshi.com on 2017/1/6.
 */
public class ImportTask {

    private Long id;

    private String path;

    public ImportTask(Long id, String path) {
        this.id = id;
        this.path = path;
    }

    public Long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }
}
