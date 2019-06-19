package brood.com.medcrawler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.Serializable;

/* Certain objects are not Serializable, mark those as 'transient'
 * ie: transient private Thread <name> */
public class UserObject implements Serializable {
    private String userId;
    private String userSpecId;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String state;
    private String userSpec;
    private String userSubspec;
    private String userProff;
    private String physicianInst;
    private Boolean soiAlabama = false;
    private Boolean soiFlorida = false;
    private Boolean soiGeorgia = false;
    private Boolean soiKentucky = false;
    private Boolean soiMissouri = false;
    private Boolean soiNc = false;
    private Boolean soiSc = false;
    private Boolean soiTennessee = false;
    private Boolean needClerkship;
    private String dateNeedClerkship;
    private Boolean needResidency;
    private String dateNeedResidency;
    private Boolean needFellowship;
    private String dateNeedFellowship;
    private Boolean needJob;
    private String dateNeedJob;
    private Boolean offerClerkship;
    private String dateOfferClerkship;
    private Boolean offerResidency;
    private String dateOfferResidency;
    private Boolean offerFellowship;
    private String dateOfferFellowship;
    private Boolean offerJob;
    private String dateOfferJob;
    private Boolean needFinancialHelp;
    private Boolean assistInterv;
    private String profilePictureName;
    private String dateOfReg;
    private String userCat;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserSpecId() {
        return userSpecId;
    }

    public void setUserSpecId(String userSpecId) { this.userSpecId = userSpecId;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserSpec() {
        return userSpec;
    }

    public void setUserSpec(String userSpec) {
        this.userSpec = userSpec;
    }

    public String getUserProff() {
        return userProff;
    }

    public void setUserProff(String userProff) {
        this.userProff = userProff;
    }

    public String getPhysicianInst() {
        return physicianInst;
    }

    public void setPhysicianInst(String physicianInst) {
        this.physicianInst = physicianInst;
    }

    public String getUserSubspec() {
        return userSubspec;
    }

    public void setUserSubspec(String userSubspec) {
        this.userSubspec = userSubspec;
    }

    public Boolean getSoiAlabama() {
        return soiAlabama;
    }

    public void setSoiAlabama(Boolean soiAlabama) {
        this.soiAlabama = soiAlabama;
    }

    public Boolean getSoiFlorida() {
        return soiFlorida;
    }

    public void setSoiFlorida(Boolean soiFlorida) {
        this.soiFlorida = soiFlorida;
    }

    public Boolean getSoiGeorgia() {
        return soiGeorgia;
    }

    public void setSoiGeorgia(Boolean soiGeorgia) {
        this.soiGeorgia = soiGeorgia;
    }

    public Boolean getSoiKentucky() {
        return soiKentucky;
    }

    public void setSoiKentucky(Boolean soiKentucky) {
        this.soiKentucky = soiKentucky;
    }

    public Boolean getSoiMissouri() {
        return soiMissouri;
    }

    public void setSoiMissouri(Boolean soiMissouri) {
        this.soiMissouri = soiMissouri;
    }

    public Boolean getSoiNc() {
        return soiNc;
    }

    public void setSoiNc(Boolean soiNc) {
        this.soiNc = soiNc;
    }

    public Boolean getSoiSc() {
        return soiSc;
    }

    public void setSoiSc(Boolean soiSc) {
        this.soiSc = soiSc;
    }

    public Boolean getSoiTennessee() {
        return soiTennessee;
    }

    public void setSoiTennessee(Boolean soiTennessee) {
        this.soiTennessee = soiTennessee;
    }

    public Boolean getNeedClerkship() {
        return needClerkship;
    }

    public void setNeedClerkship(Boolean needClerkship) {
        this.needClerkship = needClerkship;
    }

    public String getDateNeedClerkship() {
        return dateNeedClerkship;
    }

    public void setDateNeedClerkship(String dateNeedClerkship) {
        this.dateNeedClerkship = dateNeedClerkship;
    }

    public Boolean getNeedResidency() {
        return needResidency;
    }

    public void setNeedResidency(Boolean needResidency) {
        this.needResidency = needResidency;
    }

    public String getDateNeedResidency() {
        return dateNeedResidency;
    }

    public void setDateNeedResidency(String dateNeedResidency) {
        this.dateNeedResidency = dateNeedResidency;
    }

    public Boolean getNeedFellowship() {
        return needFellowship;
    }

    public void setNeedFellowship(Boolean needFellowship) {
        this.needFellowship = needFellowship;
    }

    public String getDateNeedFellowship() {
        return dateNeedFellowship;
    }

    public void setDateNeedFellowship(String dateNeedFellowship) {
        this.dateNeedFellowship = dateNeedFellowship;
    }

    public Boolean getNeedJob() {
        return needJob;
    }

    public void setNeedJob(Boolean needJob) {
        this.needJob = needJob;
    }

    public String getDateNeedJob() {
        return dateNeedJob;
    }

    public void setDateNeedJob(String dateNeedJob) {
        this.dateNeedJob = dateNeedJob;
    }

    public Boolean getOfferClerkship() {
        return offerClerkship;
    }

    public void setOfferClerkship(Boolean offerClerkship) {
        this.offerClerkship = offerClerkship;
    }

    public String getDateOfferClerkship() {
        return dateOfferClerkship;
    }

    public void setDateOfferClerkship(String dateOfferClerkship) {
        this.dateOfferClerkship = dateOfferClerkship;
    }

    public Boolean getOfferResidency() {
        return offerResidency;
    }

    public void setOfferResidency(Boolean offerResidency) {
        this.offerResidency = offerResidency;
    }

    public String getDateOfferResidency() {
        return dateOfferResidency;
    }

    public void setDateOfferResidency(String dateOfferResidency) {
        this.dateOfferResidency = dateOfferResidency;
    }

    public Boolean getOfferFellowship() {
        return offerFellowship;
    }

    public void setOfferFellowship(Boolean offerFellowship) {
        this.offerFellowship = offerFellowship;
    }

    public String getDateOfferFellowship() {
        return dateOfferFellowship;
    }

    public void setDateOfferFellowship(String dateOfferFellowship) {
        this.dateOfferFellowship = dateOfferFellowship;
    }

    public Boolean getOfferJob() {
        return offerJob;
    }

    public void setOfferJob(Boolean offerJob) {
        this.offerJob = offerJob;
    }

    public String getDateOfferJob() {
        return dateOfferJob;
    }

    public void setDateOfferJob(String dateOfferJob) {
        this.dateOfferJob = dateOfferJob;
    }

    public Boolean getNeedFinancialHelp() {
        return needFinancialHelp;
    }

    public void setNeedFinancialHelp(Boolean needFinancialHelp) {
        this.needFinancialHelp = needFinancialHelp;
    }

    public Boolean getAssistInterv() {
        return assistInterv;
    }

    public void setAssistInterv(Boolean assistInterv) {
        this.assistInterv = assistInterv;
    }

    public String getProfilePicture() {
        return profilePictureName;
    }

    public void setProfilePicture(String photoName) {

        this.profilePictureName = photoName;
    }
    public void setDateOfReg(String dateOfReg) {
        this.dateOfReg = dateOfReg;
    }

    public String getDateOfReg() {
        return this.dateOfReg;
    }

    public String getUserCat() { return this.userCat; }

    protected void setUserCat(String userCat) {
        this.userCat = userCat;
    }
}
