/**
 * @author jakubvacek
 */
package Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.sql.Timestamp;
import java.util.Objects;

//This class represents reservation of user
@DatabaseTable(tableName = "Reservations")
public class Reservation {

    @DatabaseField(canBeNull = false, foreign = true, columnName = "ownerID")
    private User owner;
    @DatabaseField()
    private Timestamp createdOn;
    @DatabaseField()
    private Timestamp duration;
    @DatabaseField()
    private Timestamp date;
    @DatabaseField(generatedId = true)
    private int ID;

    public Reservation(User owner, Timestamp createdOn, Timestamp date, Timestamp duration, int ID) {
        this.owner = owner;
        this.createdOn = createdOn;
        this.duration = duration;
        this.ID = ID;
        this.date = date;
    }

    public Reservation() {
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public Timestamp getDuration() {
        return duration;
    }

    public void setDuration(Timestamp duration) {
        this.duration = duration;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Reservation{" + "owner=" + owner + ", createdOn=" + createdOn + ", duration=" + duration + ", date=" + date + ", ID=" + ID + '}';
    }

    

    
    
    
}
