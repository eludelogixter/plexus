package brood.com.medcrawler;

/**
 * Created by nactus on 9/28/15.
 */
public class RegFeedback {

    private Boolean opSuccess = false;
    private String feedbackText;


    // Getters & Setters
    public String getFeedbackText() {
        return feedbackText;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public Boolean getOpSuccess() {
        return opSuccess;
    }

    public void setOpSuccess(Boolean opSuccess) {
        this.opSuccess = opSuccess;
    }


}
