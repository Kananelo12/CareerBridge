package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author kanan
 */
public class Internship implements Serializable {

    private int internshipId;
    private int companyId;
    private String title;
    private String category;
    private String location;
    private String description;
    private LocalDateTime postedDate;
    private String requirements;
    private int studentId;
    private String status;
    private Company company;

    // formatted date for the presentation layer
    private Date postedDateAsDate;

    // No-argument constructor
    public Internship() {
    }

    // Parameterized constructor
    public Internship(int internshipId, int companyId, String title, String category, String location, String description, LocalDateTime postedDate, String requirements, int studentId, String status) {
        this.internshipId = internshipId;
        this.companyId = companyId;
        this.title = title;
        this.category = category;
        this.location = location;
        this.description = description;
        this.postedDate = postedDate;
        this.requirements = requirements;
        this.studentId = studentId;
        this.status = status;
    }

    public int getInternshipId() {
        return internshipId;
    }

    public void setInternshipId(int internshipId) {
        this.internshipId = internshipId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDateTime postedDate) {
        this.postedDate = postedDate;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Date getPostedDateAsDate() {
        return postedDateAsDate;
    }

    public void setPostedDateAsDate(Date postedDateAsDate) {
        this.postedDateAsDate = postedDateAsDate;
    }

}
