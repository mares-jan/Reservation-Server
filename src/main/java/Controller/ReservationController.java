/**
 * @author jakubvacek
 */
package Controller;

import Core.StatusException;
import Model.Reservation;
import Service.ReservationService;
import Service.UserService;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReservationController {

    private static final Logger LOG = Logger.getLogger(ReservationController.class.getName());
    @Resource
    private ReservationService reservationService;
    @Resource
    private UserService userService;

    @CrossOrigin(origins = "http://localhost:8383")
    @RequestMapping(value = "/reservations", method = RequestMethod.POST)
    public ResponseEntity<Void> createReservation(@RequestBody Reservation reservation, @RequestParam(value = "userId") int userId) {
        try {
            LOG.log(Level.INFO, "Creating reservation");
            System.out.println(reservation);
            reservation.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
            reservation.setOwner(userService.getUserById(userId));
            reservationService.createReservation(reservation);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (StatusException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.status);
        }
    }

    @CrossOrigin(origins = "http://localhost:8383")
    @RequestMapping(value = "/reservations", method = RequestMethod.GET, params = "userId")
    public ResponseEntity<ArrayList<Reservation>> getReservationsOfUser(@RequestParam(value = "userId") int userId) {
        ArrayList<Reservation> reservations;
        try {
            LOG.log(Level.INFO, "Geting reservations of user with id: {0}", userId);
            //Get items and sort them acording to createdOn then map items to view models
            reservations = reservationService.getReservationsOfUser(userId)
                    .stream().sorted((first, second)
                            -> first.getCreatedOn().compareTo(second.getCreatedOn()))
                    .collect(Collectors.toCollection(ArrayList::new));
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch (StatusException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.status);
        }
    }
     @CrossOrigin(origins = "http://localhost:8383")
    @RequestMapping(value = "/reservations", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteReservation(@RequestParam(value = "reservationId") int reservationId) {
        try {
            LOG.log(Level.INFO, "Deleting reservation with id: {0}", reservationId);
            reservationService.deleteReservationById(reservationId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (StatusException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.status);
        }
    }

}
