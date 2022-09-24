package ex.rr.scheduling.controller;

import static ex.rr.scheduling.controller.Utils.hasModeratorRole;

import ex.rr.scheduling.model.Session;
import ex.rr.scheduling.model.Settings;
import ex.rr.scheduling.model.User;
import ex.rr.scheduling.model.enums.SettingsSubTypeEnum;
import ex.rr.scheduling.payload.request.SessionUserRequest;
import ex.rr.scheduling.repository.SessionRepository;
import ex.rr.scheduling.repository.SettingsRepository;
import ex.rr.scheduling.repository.UserRepository;
import java.util.Collection;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/location/{id}/session")
public class SessionController {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SettingsRepository settingsRepository;

    @PostMapping("/addUser")
    public ResponseEntity<String> addSession(HttpServletRequest request
            , @PathVariable Integer id
            , @RequestBody @Validated SessionUserRequest sessionRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        Optional<Session> session = sessionRepository.findById(sessionRequest.getSessionId());

        Collection<Settings> maxUsers = settingsRepository.findByLocationIdAndSubType(id,
                SettingsSubTypeEnum.MAX_USERS);
        if (maxUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Number of users not configured.");
        }

        Optional<User> user = userRepository.findByUsername(sessionRequest.getUsername());

        if (session.isPresent() && user.isPresent()) {
            if (session.get().getCount() >= Integer.parseInt(maxUsers.stream().findFirst().get().getVal())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Max users reached for this session.");
            }

            if (!sessionRequest.getUsername().equals(username)) {
                if (!hasModeratorRole()) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
                session.get().addUser(user.get());
            } else {
                session.get().addUser(user.get());
            }
            sessionRepository.save(session.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> removeSession(HttpServletRequest request
            , @RequestBody @Validated SessionUserRequest sessionRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        Optional<Session> session = sessionRepository.findById(sessionRequest.getSessionId());
        Optional<User> user = userRepository.findByUsername(sessionRequest.getUsername());

        if (session.isPresent() && user.isPresent()) {
            if (!sessionRequest.getUsername().equals(username)) {
                if (!hasModeratorRole()) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
                session.get().removeUser(user.get());
            } else {
                session.get().removeUser(user.get());
            }
            sessionRepository.save(session.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PatchMapping("/{sessionId}/lock")
    public ResponseEntity<String> lockSession(@PathVariable Integer sessionId) {
        Optional<Session> session = sessionRepository.findById(sessionId);
        if (session.isPresent()) {
            session.get().setActive(false);
            sessionRepository.save(session.get());
            return ResponseEntity.ok("Session locked.");
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{sessionId}/unlock")
    public ResponseEntity<String> unlockSession(@PathVariable Integer sessionId) {
        Optional<Session> session = sessionRepository.findById(sessionId);
        if (session.isPresent()) {
            session.get().setActive(false);
            sessionRepository.save(session.get());
            return ResponseEntity.ok("Session locked.");
        }
        return ResponseEntity.notFound().build();
    }

}
