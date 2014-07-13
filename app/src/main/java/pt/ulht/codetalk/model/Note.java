package pt.ulht.codetalk.model;

import java.util.Date;

/**
 * Created by Joao on 12/07/2014.
 */
public class Note {
    private String code;
    private String content;
    private String createdAt;
    private String createdBy;
    private String title;

    @SuppressWarnings("unused")
    private Note() { }

    @SuppressWarnings("unused")
    Note(String code, String content, String createdAt, String createdBy, String title) {
        this.code = code;
        this.content = content;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.title = title;
    }

    public String getCode() { return code; }
    public String getContent() { return content; }
    public String getCreatedAt() { return createdAt; }
    public String getCreatedBy() { return createdBy; }
    public String getTitle() { return title; }

}
/*
    private String createdBy;
    private String fullName;
    private String mode;
    private String name;

    @SuppressWarnings("unused")
    private AllowedGroup() { }

    AllowedGroup(String createdBy, String fullName, String mode, String name) {
        this.createdBy = createdBy;
        this.fullName = fullName;
        this.mode = mode;
        this.name = name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getFullName() {
        return fullName;
    }

    public String getMode() {
        return mode;
    }

    public String getName() {
        return name;
    }
 */
