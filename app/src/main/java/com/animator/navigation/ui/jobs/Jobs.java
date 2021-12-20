package com.animator.navigation.ui.jobs;

public class Jobs {
    String id;
    String jobImage;
    String jobTitle;
    String jobDesignation;

//    public Jobs(String jobImage, String jobTitle) {
//        this.jobImage = jobImage;
//        this.jobTitle = jobTitle;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobImage() {
        return jobImage;
    }

    public void setJobImage(String jobImage) {
        this.jobImage = jobImage;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDesignation() {
        return jobDesignation;
    }

    public void setJobDesignation(String jobDesignation) {
        this.jobDesignation = jobDesignation;
    }
}
