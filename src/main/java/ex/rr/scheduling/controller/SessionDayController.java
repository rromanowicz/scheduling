package ex.rr.scheduling.controller;

import ex.rr.scheduling.model.Location;
import ex.rr.scheduling.payload.request.DateRange;
import ex.rr.scheduling.repository.LocationRepository;
import ex.rr.scheduling.repository.SessionDayRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/location/{id}/sessionDays")
public class SessionDayController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private SessionDayRepository sessionDayRepository;


    @PatchMapping("/unlock")
    public ResponseEntity<DateRange> unlockSessionDay(@PathVariable Integer id, @RequestBody DateRange dates) {
        Optional<Location> location = locationRepository.findById(id);
        if (location.isPresent()) {
            location.get().getSessionYears().stream()
                    .filter(sessionYear -> dates.getYears().contains(sessionYear.getSessionYear()))
                    .forEach(sessionYear -> sessionYear.getSessionMonths()
                            .forEach(sessionMonth -> sessionMonth.getSessionDays()
                                    .stream()
                                    .filter(sessionDay -> dates.getDateList().contains(sessionDay.getSessionDate())
                                            && !sessionDay.isActive())
                                    .forEach(sessionDay -> {
                                        sessionDay.setActive(true);
                                        sessionDay.getSessions().forEach(session -> session.setActive(true));
                                    })
                            ));
            locationRepository.save(location.get());
            return ResponseEntity.ok(dates);
        }

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/lock")
    public ResponseEntity<DateRange> lockSessionDay(@PathVariable Integer id, @RequestBody DateRange dates) {
        Optional<Location> location = locationRepository.findById(id);
        if (location.isPresent()) {
            location.get().getSessionYears().stream()
                    .filter(sessionYear -> dates.getYears().contains(sessionYear.getSessionYear()))
                    .forEach(sessionYear -> sessionYear.getSessionMonths()
                            .forEach(sessionMonth -> sessionMonth.getSessionDays()
                                    .stream()
                                    .filter(sessionDay -> dates.getDateList().contains(sessionDay.getSessionDate()))
                                    .forEach(sessionDay -> {
                                        sessionDay.setActive(false);
                                        sessionDay.getSessions().forEach(session -> session.setActive(false));
                                    })
                            ));
            locationRepository.save(location.get());
            return ResponseEntity.ok(dates);
        }

        return ResponseEntity.notFound().build();
    }

}
