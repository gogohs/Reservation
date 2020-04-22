package market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

 @RestController
 public class ReservationController {

     @Autowired
     ReservationRepository reservationRepository;

     @PostMapping("/reservation")
     Reservation reservationInsert(@RequestBody Reserved reserved) {

         Reservation reservation = new Reservation();

         reservation.setProductId(reserved.getProductId());
         reservation.setReservationStatus(reserved.getReservationStatus());
         reservation.setType("Reserved");
         return reservationRepository.save(reservation);
     }

     @RequestMapping(method = RequestMethod.PATCH, path = "/reservationupdate")
     public void reservationCancel(@RequestBody ReservationCanceled reservationCanceled) {
         Reservation reservation = new Reservation();

         reservation.setReservationStatus(reservationCanceled.getReservationStatus());
         reservation.setId(reservationCanceled.getId());
         reservation.setProductId(reservationCanceled.getProductId());

         reservation.setType("Cancled");

         reservationRepository.save(reservation);

     }

 }
