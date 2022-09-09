package ex.rr.scheduling.controller;

import ex.rr.scheduling.model.Session;
import ex.rr.scheduling.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/location/{id}/session")
public class SessionController {

    @Autowired
    private SessionRepository sessionRepository;

    @PostMapping("/addSession")
    public ResponseEntity<Session> addSession(@PathVariable Integer id, LocalDate sessionDate, LocalTime sessionTime, Integer userId) {

//        sessionRepository.findById()

        return ResponseEntity.badRequest().build();
    }


}
