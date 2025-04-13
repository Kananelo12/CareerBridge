package model;

import java.time.LocalDateTime;

/**
 *
 * @author kanan
 */
public class Feedback {
    private int feedbackId;
    private int studentId;
    private int internshipId;
    private String rating;
    private String comments;
    private LocalDateTime feedbackDate;
    
    // No-arguments constructor
    public Feedback() {}

    public Feedback(int feedbackId, int studentId, int internshipId, String rating, String comments, LocalDateTime feedbackDate) {
        this.feedbackId = feedbackId;
        this.studentId = studentId;
        this.internshipId = internshipId;
        this.rating = rating;
        this.comments = comments;
        this.feedbackDate = feedbackDate;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getInternshipId() {
        return internshipId;
    }

    public void setInternshipId(int internshipId) {
        this.internshipId = internshipId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(LocalDateTime feedbackDate) {
        this.feedbackDate = feedbackDate;
    }
    
    
    
}
