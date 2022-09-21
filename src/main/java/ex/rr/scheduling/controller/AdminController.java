package ex.rr.scheduling.controller;

import ex.rr.scheduling.model.User;
import ex.rr.scheduling.repository.SessionDayRepository;
import ex.rr.scheduling.repository.SessionRepository;
import ex.rr.scheduling.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionDayRepository sessionDayRepository;

    @Autowired
    private SessionRepository sessionRepository;


    @PatchMapping("/user/{id}/enabled")
    public ResponseEntity<String> lockUser(@PathVariable Integer id, @RequestParam boolean enabled) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setEnabled(enabled);
            userRepository.save(user.get());
            return ResponseEntity.ok(String.format("User [%s] Enabled: [%s]", user.get().getUsername(), enabled));
        }
        return ResponseEntity.notFound().build();
    }

}
