package pt.ulht.codetalk.model;

public class AllowedGroup {

    private String createdBy;
    private String fullName;
    private String mode;
    private String name;

    @SuppressWarnings("unused")
    private AllowedGroup() { }
    
    @SuppressWarnings("unused")
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
}
