package entity;

public class User {
    private long userId;
    private String username;
    private String userPassword;
    private String userFullName;
    private UserRole userRole;

    public User() {
    }

    public User(long userId, String username, String userPassword, String userFullName, UserRole userRole) {
        this.userId = userId;
        this.username = username;
        this.userPassword = userPassword;
        this.userFullName = userFullName;
        this.userRole = userRole;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "UserID: " + userId + " | Username: " + username + " | FullName: " + userFullName + " | Role: " + userRole;
    }
}
