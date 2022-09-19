package ex.rr.scheduling.controller;

import ex.rr.scheduling.model.Session;
import ex.rr.scheduling.model.Settings;
import ex.rr.scheduling.model.User;
import ex.rr.scheduling.model.enums.RoleEnum;
import ex.rr.scheduling.model.enums.SettingsSubTypeEnum;
import ex.rr.scheduling.payload.request.SessionUserRequest;
import ex.rr.scheduling.repository.SessionRepository;
import ex.rr.scheduling.repository.SettingsRepository;
import ex.rr.scheduling.repository.UserRepository;
import ex.rr.scheduling.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/location/{id}/session")
public class SessionController {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/addUser")
    public ResponseEntity<String> addSession(HttpServletRequest request
            , @PathVariable Integer id
            , @RequestBody @Validated SessionUserRequest sessionRequest) {
        String jwt = jwtUtils.getJwtFromCookies(request);
        if (jwtUtils.validateJwtToken(jwt)) {

            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Optional<Session> session = sessionRepository.findById(sessionRequest.getSessionId());

            Collection<Settings> maxUsers = settingsRepository.findByLocationIdAndSubType(id, SettingsSubTypeEnum.MAX_USERS);
            if (maxUsers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Number of users not configured.");
            }

            Optional<User> callingUser = userRepository.findByUsername(username);

            if (session.isPresent() && callingUser.isPresent()) {
                if (session.get().getCount() >= Integer.parseInt(maxUsers.stream().findFirst().get().getVal())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Max users reached for this session.");
                }

                if (!sessionRequest.getUsername().equals(username)) {
                    Optional<User> user = userRepository.findByUsername(sessionRequest.getUsername());
                    if(user.isPresent() && callingUser.get().hasRole(RoleEnum.ROLE_MODERATOR)) {
                        session.get().addUser(user.get());
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                    }
                } else {
                    session.get().addUser(callingUser.get());
                }
                sessionRepository.save(session.get());
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }

        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> removeSession(HttpServletRequest request
            , @RequestBody @Validated SessionUserRequest sessionRequest) {
        String jwt = jwtUtils.getJwtFromCookies(request);
        if (jwtUtils.validateJwtToken(jwt)) {
            String username = jwtUtils.getUserNameFromJwtToken(jwt);
            Optional<Session> session = sessionRepository.findById(sessionRequest.getSessionId());
            Optional<User> callingUser = userRepository.findByUsername(username);

            if (session.isPresent() && callingUser.isPresent()) {
                if (!sessionRequest.getUsername().equals(username)) {
                    Optional<User> user = userRepository.findByUsername(sessionRequest.getUsername());
                    if(user.isPresent() && callingUser.get().hasRole(RoleEnum.ROLE_MODERATOR)) {
                        session.get().removeUser(user.get());
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                    }
                } else {
                    session.get().removeUser(callingUser.get());
                }
                sessionRepository.save(session.get());
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }


}
