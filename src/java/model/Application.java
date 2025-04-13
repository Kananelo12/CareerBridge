package model;

import java.time.LocalDateTime;

/**
 *
 * @author kanan
 */
public class Application {
    private int applicationId;
    private int studentId;
    private int internshipId;
    private String cvUrl;
    private String transcriptUrl;
    private LocalDateTime applicationDate;
    private String status;
    
    // No-arguments constructor
    public Application() {}
    
    // Parameterized constructor

    public Application(int applicationId, int studentId, int internshipId, String cvUrl, String transcriptUrl, LocalDateTime applicationDate, String status) {
        this.applicationId = applicationId;
        this.studentId = studentId;
        this.internshipId = internshipId;
        this.cvUrl = cvUrl;
        this.transcriptUrl = transcriptUrl;
        this.applicationDate = applicationDate;
        this.status = status;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
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

    public String getCvUrl() {
        return cvUrl;
    }

    public void setCvUrl(String cvUrl) {
        this.cvUrl = cvUrl;
    }

    public String getTranscriptUrl() {
        return transcriptUrl;
    }

    public void setTranscriptUrl(String transcriptUrl) {
        this.transcriptUrl = transcriptUrl;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
