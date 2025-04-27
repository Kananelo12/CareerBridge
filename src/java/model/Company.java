package model;

import java.io.Serializable;

/**
 *
 * @author kanan
 */
public class Company implements Serializable {

    private int companyId;
    private String companyName;
    private String description;
    private String industry;
    private String website;
    private String logoUrl;
    private String location;
    private String contactInfo;
    private Internship internship;

    // No-arguments constructor
    public Company() {
    }

    // Parameterized constructor
    public Company(int companyId, String companyName, String description, String industry, String website, String logoUrl, String location, String contactInfo) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.description = description;
        this.industry = industry;
        this.website = website;
        this.logoUrl = logoUrl;
        this.location = location;
        this.contactInfo = contactInfo;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Internship getInternship() {
        return internship;
    }

    public void setInternship(Internship internship) {
        this.internship = internship;
    }

}
