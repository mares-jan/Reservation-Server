/**
 * @author jakubvacek
 */

package Model.TransferObjects;

import Model.User;

public class UserTo {
    private final int ID;
    private final String username;
    private final String role;

    public UserTo(User user){
    this.ID = user.getID();
    this.username = user.getUsername();
    this.role = user.getRole();
    }

    public int getID() {
        return ID;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "UserTo{" + "ID=" + ID + ", username=" + username + ", role=" + role + '}';
    }

    
 
}
