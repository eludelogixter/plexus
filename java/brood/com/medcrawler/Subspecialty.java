package brood.com.medcrawler;

public class Subspecialty {
    private String subspecName;
    private String subspecId;
    private Boolean selected = false;


    public Subspecialty(String subspecName, String subspecId) {
        this.subspecName = subspecName;
        this.subspecId = subspecId;
    }

    public String getSubspecName() {
        return subspecName;
    }

    public String getSubspecId() {
        return subspecId;
    }

    public Boolean getSelected() {
        return selected;
    }
    public void setSelected(Boolean value) {
        selected = value;
    }

}
