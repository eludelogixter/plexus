package brood.com.medcrawler;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * StringProcessor is used to process all the fields for user registration
 */
public class StringProcessor {

    private List<RegFeedback> retValue;
    private RegFeedback tempFeedback;

    public static enum Mode {
        EMAIL, PASSWORD, REPPASSWORD, REGKEY, FIRSTNAME, LASTNAME, ADDRESS, ZIP, CITY, INST
    }

    public StringProcessor() {

        // Call the processor module
        //processInput(mode, value);
    }

    protected List<RegFeedback> processInput(Mode mode, String value) {
        retValue = new ArrayList<>();

        // Check the type of input
        switch (mode) {
            case EMAIL:

                /* Code for email verification */
                // 1. Field can't be empty
                if((value.isEmpty()) || (value.equals(" "))) {

                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("Email can't be empty");
                    retValue.add(tempFeedback);
                }

                // 2. Email format is (in)correct
                if (!(android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches())) {

                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("Email format is incorrect");
                    retValue.add(tempFeedback);
                }

                break;

            case PASSWORD:

                /* Code for password verification */
                // 1. Field can't be empty
                if((value.isEmpty()) || value.equals(" ")) {

                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("Password can't be empty");
                    retValue.add(tempFeedback);
                }

                // 2. Field can't contain less than 8 characters
                if(value.length() < 8) {

                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("Password can't be less than 8 characters");
                    retValue.add(tempFeedback);
                }

                // 3. Field can't contain spaces
                if(value.contains(" ")) {
                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("Password can't contain empty spaces");
                    retValue.add(tempFeedback);
                }

                break;

            case REPPASSWORD:

                /* Code for password verification */
                // 1. Field can't be empty
                if((value.isEmpty()) || value.equals(" ")) {

                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("Repeat Password can't be empty");
                    retValue.add(tempFeedback);
                }

                // 2. Field can't contain less than 8 characters
                if(value.length() < 8) {

                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("Repeat Password can't be less than 8 characters");
                    retValue.add(tempFeedback);
                }

                // 3. Field can't contain spaces
                if(value.contains(" ")) {
                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("Repeat Password can't contain empty spaces");
                    retValue.add(tempFeedback);
                }

                break;

            case REGKEY:

                // 1. Field can't be empty
                if((value.isEmpty()) || value.equals(" ")) {

                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("Registration Key can't be empty");
                    retValue.add(tempFeedback);
                }

                // 2. Field can't contain less than 8 characters
                if(value.length() < 8) {

                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("Registration Key format is incorrect");
                    retValue.add(tempFeedback);
                }
                break;

            case FIRSTNAME:

                // Code for first & last name verification
                // 1. Field can't be empty
                if(value.isEmpty()) {
                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("First Name can't be empty");
                    retValue.add(tempFeedback);
                }

                // 2. Field can only contain 50 chars
                if(value.length() > 50) {

                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("First Name is too long");
                    retValue.add(tempFeedback);
                }

                // 3. Field can't contain spaces
                if(value.contains("  ")) {
                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("First Name can't contain double spaces");
                    retValue.add(tempFeedback);
                }

                // 4. Field can't contain non alphabetic input
                if (StringUtils.isAlphaSpace(value)) {

                    // This is good, do nothing

                }
                else {
                    // This is bad, prompt error
                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("First Name can't contain non " +
                            "alphabetic characters");
                    retValue.add(tempFeedback);
                }

                break;

            case LASTNAME:

                // 1. Field can't be empty
                if(value.isEmpty()) {
                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("Last Name can't be empty");
                    retValue.add(tempFeedback);
                }

                // 2. Field can only contain 50 chars
                if(value.length() > 50) {

                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("Last Name is too long");
                    retValue.add(tempFeedback);
                }

                // 3. Field can't contain spaces
                if(value.contains("  ")) {
                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("Last Name can't contain double spaces");
                    retValue.add(tempFeedback);
                }

                // 4. Field can't contain non alphabetic input
                if (StringUtils.isAlphaSpace(value)) {

                    // This is good, do nothing

                }
                else {
                    // This is bad, prompt error
                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("Last Name can't contain non " +
                            "alphabetic characters");
                    retValue.add(tempFeedback);
                }

                break;

            case ADDRESS:

                // Code for address verification
                // 1. Field can only contain 50 chars
                if(value.length() > 50) {
                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("Address is too long");
                    retValue.add(tempFeedback);
                }

                break;

            case ZIP:

                // Code for zip verification
                // 1. This field can't be empty
                if(value.isEmpty()) {
                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("Zip can't be empty");
                    retValue.add(tempFeedback);
                }

                // 2. This field can't be smaller than 5 chars, or contain alphabetic characters
                if(value.length() < 5 || !StringUtils.isNumeric(value)) {
                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("Zip format is incorrect");
                    retValue.add(tempFeedback);
                }

                break;

            case CITY:

                // 1. This field can't be empty
                if(value.isEmpty()) {
                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("City can't be empty");
                    retValue.add(tempFeedback);
                }

                // 2. This field can't be longer than 50 chars.
                if(value.length() > 50) {
                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("City is too long");
                    retValue.add(tempFeedback);
                }

                // 3. Field can't contain 2 spaces
                if(value.contains("  ")) {
                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("City can't contain double spaces");
                    retValue.add(tempFeedback);
                }

                // 4. Field can't contain non alphabetic input
                if(!StringUtils.isAlphaSpace(value)) {
                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("City can't contain non " +
                            "alphabetic characters");
                    retValue.add(tempFeedback);
                }

                break;

            case INST:

                // 1. This field can't be empty
                if(value.isEmpty()) {
                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("Institution can't be empty");
                    retValue.add(tempFeedback);
                }

                // 2. This field can't be longer than 50 chars.
                if(value.length() > 100) {
                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("Institution is too long");
                    retValue.add(tempFeedback);
                }

                // 3. Field can't contain 2 spaces
                if(value.contains("  ")) {
                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("Institution can't contain double spaces");
                    retValue.add(tempFeedback);
                }

                // 4. Field can't contain non alphabetic input
                if(!StringUtils.isAlphaSpace(value)) {
                    tempFeedback = new RegFeedback();
                    tempFeedback.setOpSuccess(false);
                    tempFeedback.setFeedbackText("Institution can't contain non " +
                            "alphabetic characters");
                    retValue.add(tempFeedback);
                }

                break;

            default:

                break;

        }

        return retValue;
    }

}
