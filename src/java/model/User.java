package model;

import java.io.Serializable;

/**
 *
 * @author kanan
 */
public class User implements Serializable {
    private int userId;
    private String email;
    private String password;
    private int roleId;
    private String roleName;
    private UserDetail userDetails;
    
    // No-argument constructor
    public User() {}
    
    // Parameterized constructor
    public User(int userId, String email, String password, int roleId) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public UserDetail getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetail userDetails) {
        this.userDetails = userDetails;
    }
    
}
