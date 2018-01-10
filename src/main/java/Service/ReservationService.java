/**
 * @author jakubvacek
 */
package Service;

import Core.StatusException;
import Model.Reservation;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;

@Service
public class ReservationService {

    private static final Logger LOG = Logger.getLogger(ReservationService.class.getName());
    private final String DATABASE_URL = "jdbc:derby:\\MyDB\\demo";
    private Dao<Reservation, Integer> dao;

    public ReservationService() {
        try {
            ConnectionSource connectionSource = new JdbcConnectionSource(DATABASE_URL);
            dao = com.j256.ormlite.dao.DaoManager.createDao(connectionSource, Reservation.class);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public void createReservation(Reservation reservation) throws StatusException {
        try {
            System.out.println("create reservation");
            ArrayList<Reservation> reservations = (ArrayList<Reservation>) dao.queryForAll();
            if(reservations.size() !=0){
            boolean conflict = true;
            Timestamp start = reservation.getDate();
            Timestamp end = new Timestamp(reservation.getDate().getTime() + reservation.getDuration().getTime());
            for (Reservation r : reservations) {
                Timestamp startOther = r.getDate();
                Timestamp endOther = new Timestamp(r.getDate().getTime() + r.getDuration().getTime());
                if (start.after(endOther) || end.before(startOther)) { //no conflict
                    conflict = false;
                    System.out.println("conflict");
                }
            }
            if (conflict) {
                                    System.out.println("conflict");
                throw new StatusException(HttpStatus.CONFLICT);
            }
            }
            dao.create(reservation);
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new StatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteReservationById(int reservationId) throws StatusException {
        try {
            if (dao.idExists(reservationId)) {
                dao.deleteById(reservationId);
            } else {
                throw new StatusException(HttpStatus.NOT_FOUND);
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new StatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ArrayList<Reservation> getReservationsOfUser(int userId) throws StatusException {
        try {
            return (ArrayList<Reservation>) dao.queryBuilder().where().eq("ownerID", userId).query();
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new StatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
