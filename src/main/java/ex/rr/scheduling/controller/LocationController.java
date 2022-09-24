package ex.rr.scheduling.controller;

import static ex.rr.scheduling.controller.Utils.cleanOutput;
import static ex.rr.scheduling.controller.Utils.parseResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import ex.rr.scheduling.model.Location;
import ex.rr.scheduling.model.Settings;
import ex.rr.scheduling.repository.LocationRepository;
import ex.rr.scheduling.repository.SettingsRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/location")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private SettingsRepository settingsRepository;


    @GetMapping("")
    public ResponseEntity<String> getLocations(HttpServletRequest request) throws JsonProcessingException {
        List<Location> locations = locationRepository.findAll();
        return ResponseEntity.ok(parseResponse(cleanOutput(locations)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getLocation(HttpServletRequest request, @PathVariable Integer id)
            throws JsonProcessingException {
        List<Location> locations = locationRepository.findAllById(List.of(id));
        return ResponseEntity.ok(parseResponse(cleanOutput(locations)));
    }

    @PostMapping("/add")
    public ResponseEntity<Location> addLocation(@RequestBody String address) {
        Location location = Location.builder().address(address).build();
        log.info("Adding new location at: [{}]", address);
        return new ResponseEntity<>(locationRepository.save(location), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/addSettings")
    public ResponseEntity<List<Settings>> addLocationSettings(@RequestBody List<Settings> settings,
            @PathVariable Integer id) {
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
