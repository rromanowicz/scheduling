package ex.rr.scheduling.controller;

import ex.rr.scheduling.model.Location;
import ex.rr.scheduling.model.Settings;
import ex.rr.scheduling.model.enums.RoleEnum;
import ex.rr.scheduling.repository.LocationRepository;
import ex.rr.scheduling.repository.SettingsRepository;
import ex.rr.scheduling.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/location")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public ResponseEntity<List<Location>> getLocations(HttpServletRequest request) {
        List<Location> locations = locationRepository.findAll();
        return ResponseEntity.ok(cleanOutput(locations));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Location>> getLocation(HttpServletRequest request, @PathVariable Integer id) {
        List<Location> locations = locationRepository.findAllById(List.of(id));
        return ResponseEntity.ok(cleanOutput(locations));
    }

    private List<Location> cleanOutput(List<Location> locations) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasModeratorRole = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals(RoleEnum.ROLE_MODERATOR.name()) ||
                        r.getAuthority().equals(RoleEnum.ROLE_ADMIN.name())
                );

        if (!hasModeratorRole) {
            locations.forEach(location ->
                    location.getSessionYears().forEach(sessionYear ->
                            sessionYear.getSessionMonths().forEach(sessionMonth ->
                                    sessionMonth.getSessionDays().forEach(sessionDay ->
                                            sessionDay.getSessions().forEach(session ->
                                                    session.getUsers().removeIf(user ->
                                                            !authentication.getName().equals(user.getUsername()))
                                            )))));
        }

        locations.forEach(location ->
                location.getSessionYears().forEach(sessionYear ->
                        sessionYear.getSessionMonths().removeIf(sessionMonth ->
                                sessionMonth.getMonthDate().isBefore(LocalDate.now().withDayOfMonth(1))
                        )));

        return locations;
    }

    @PostMapping("/add")
    public ResponseEntity<Location> addLocation(@RequestBody String address) {
        Location location = Location.builder().address(address).build();
        log.info("Adding new location at: [{}]", address);
        return new ResponseEntity<>(locationRepository.save(location), HttpStatus.CREATED);
    }


    @PostMapping("/{id}/addSettings")
    public ResponseEntity<List<Settings>> addLocationSettings(@RequestBody List<Settings> settings, @PathVariable Integer id) {
        Optional<Location> location = locationRepository.findById(id);
        if (location.isPresent()) {
            settings.forEach(setting -> {
                setting.setLocationId(id);
                if (setting.getSubType().isUnique()) {
                    List<Settings> settingsList = settingsRepository.findByLocationId(id)
                            .stream().filter(set ->
                                    setting.getSubType().equals(set.getSubType()) &&
                                            setting.getType().equals(set.getType())).collect(Collectors.toList());
                    settingsRepository.deleteAll(settingsList);
                }
            });

            log.info("Saving settings for location: [{}]", id);
            return new ResponseEntity<>(settingsRepository.saveAll(settings), HttpStatus.CREATED);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
