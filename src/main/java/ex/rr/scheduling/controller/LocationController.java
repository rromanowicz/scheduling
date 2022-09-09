package ex.rr.scheduling.controller;

import ex.rr.scheduling.model.Location;
import ex.rr.scheduling.model.Settings;
import ex.rr.scheduling.repository.LocationRepository;
import ex.rr.scheduling.repository.SettingsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("")
    public ResponseEntity<List<Location>> getLocations() {
        return ResponseEntity.ok(locationRepository.findAll());
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
