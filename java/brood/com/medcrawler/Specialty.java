package brood.com.medcrawler;

public class Specialty {
    private String specName;
    private String specId;
    private Boolean selected = false;


    public Specialty(String specName, String specId) {
        this.specName = specName;
        this.specId = specId;
    }

    public String getSpecName() {
        return specName;
    }

    public String getSpecId() {
        return specId;
    }

    public Boolean getSelected() {
        return selected;
    }
    public void setSelected(Boolean value) {
        selected = value;
    }

}
