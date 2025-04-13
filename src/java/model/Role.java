package model;

import java.io.Serializable;

/**
 *
 * @author kanan
 */
public class Role implements Serializable {

    private int roleId;
    private String roleName;

    // No-argument constructor
    public Role() {
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

}
