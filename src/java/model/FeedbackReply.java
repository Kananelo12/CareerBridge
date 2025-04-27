package model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author kanan
 */
public class FeedbackReply implements Serializable {

    private int replyId;
    private int feedbackId;
    private String replyText;
    private LocalDateTime replyDate;

    public FeedbackReply() {
    }

    public FeedbackReply(int replyId, int feedbackId, String replyText, LocalDateTime replyDate) {
        this.replyId = replyId;
        this.feedbackId = feedbackId;
        this.replyText = replyText;
        this.replyDate = replyDate;
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    public LocalDateTime getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(LocalDateTime replyDate) {
        this.replyDate = replyDate;
    }

}
