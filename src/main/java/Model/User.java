/**
 * @author jakubvacek
 */

package Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

//This class represents user
@DatabaseTable(tableName = "users")
public class User {

    @DatabaseField(generatedId = true)
    private int ID;
    @DatabaseField(uniqueCombo = true, canBeNull = false)
    private String username;
    @DatabaseField(uniqueCombo = true, canBeNull = false)
    private String passwordHash;
    @DatabaseField(canBeNull = false)
    private String role;

    public User() {
    }

    public User(int ID, String username, String passwordHash, String role) {
        this.ID = ID;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" + "ID=" + ID + ", username=" + username + ", passwordHash=" + passwordHash + ", role=" + role + '}';
    }
    
    
    
    
    
    
}
