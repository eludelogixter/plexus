package brood.com.medcrawler;

/**
 * Created by spirus on 03/07/15.
 */
public class UserAdminMain {

    private String name;
    private String category;
    private int photoId;
    private String spec;
    private String subSpec;

    public UserAdminMain(String name, String category, String spec, String subSpec, int photoId) {
        this.name = name;
        this.category = category;
        this.spec = spec;
        this.subSpec = subSpec;
        this.photoId = photoId;
    }

    public String getName() {
        return name;
    }


    public String getCategory() {
        return category;
    }

    public String getSpec() {
        return spec;
    }

    public String getSubSpec() {
        return subSpec;
    }

    public int getPhotoId() {
        return photoId;
    }

}
