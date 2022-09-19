package ex.rr.scheduling.controller;

import ex.rr.scheduling.payload.request.DateRange;
import ex.rr.scheduling.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/location/{id}/date")
public class SessionDateController {

    @Autowired
    private LocationRepository locationRepository;


    @PostMapping("/addSession")
    public ResponseEntity<DateRange> addSession(@PathVariable Integer id, @RequestBody DateRange dates) {
//TODO
        return ResponseEntity.internalServerError().build();
    }


    @DeleteMapping("/removeSession")
    public ResponseEntity<DateRange> removeSession(@PathVariable Integer id, @RequestBody DateRange dates) {
//TODO
        return ResponseEntity.internalServerError().build();
    }

}
