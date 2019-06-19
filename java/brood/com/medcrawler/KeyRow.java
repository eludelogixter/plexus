package brood.com.medcrawler;

public class KeyRow {

    private String key;
    private String keyId;
    private String userCategory;
    private Boolean checkboxValue; ;


    public KeyRow(String key, String keyId, String userCategory, Boolean checkboxValue) {
        this.key = key;
        this.keyId = keyId;
        this.userCategory = userCategory;
        this.checkboxValue = checkboxValue;
    }

    public String getKey() {
        return key;
    }

    public String getKeyId() {
        return keyId;
    }

    public String getUserCategory() {
        return userCategory;
    }

    public Boolean getCheckBoxValue() {
        return checkboxValue;
    }

    public void setCheckboxValue(Boolean value) {
        this.checkboxValue = value;
    }

}
