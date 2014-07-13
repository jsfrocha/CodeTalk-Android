package pt.ulht.codetalk.model;

public class FollowedGroup {

    private String fullName;
    private String mode;
    private String name;

    @SuppressWarnings("unused")
    private FollowedGroup() { }

    @SuppressWarnings("unused")
    FollowedGroup(String fullName, String mode, String name) {
        this.fullName = fullName;
        this.mode = mode;
        this.name = name;
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
