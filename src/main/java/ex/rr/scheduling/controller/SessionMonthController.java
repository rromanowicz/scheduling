package ex.rr.scheduling.controller;

import ex.rr.scheduling.model.*;
import ex.rr.scheduling.model.enums.SettingsSubTypeEnum;
import ex.rr.scheduling.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.time.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/location/{id}/month")
public class SessionMonthController {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private SessionYearRepository sessionYearRepository;
    @Autowired
    private SessionMonthRepository sessionMonthRepository;
    @Autowired
    private SessionDateRepository sessionDateRepository;
    @Autowired
    private SettingsRepository settingsRepository;


    @PostMapping("/add")
    public ResponseEntity<SessionMonth> addSessionMonth(@PathVariable Integer id, @NotNull @RequestParam Integer year
            , @NotNull @RequestParam Integer month) {
        Optional<SessionYear> sessionYear = sessionYearRepository.findByLocationIdAndSessionYear(id, year);
        SessionYear savedSessionYear = sessionYear.orElseGet(() ->
                sessionYearRepository.save(SessionYear.builder().locationId(id).sessionYear(year).sessionMonths(List.of()).build()));

        if (savedSessionYear.getSessionMonths().stream().noneMatch(mth -> Month.of(month).equals(mth.getMonthName()))) {
            try {
                SessionMonth sessionMonth = sessionMonthRepository.save(SessionMonth.builder().sessionYearId(savedSessionYear.getId()).monthName(Month.of(month)).build());
                log.info("Adding calendar for location [{}], [{} {}]", id, year, Month.of(month));
                return new ResponseEntity<>(sessionMonthRepository.save(getFilledMonth(id, sessionMonth, year, month)), HttpStatus.CREATED);
            } catch (DateTimeException dte) {
                log.error("Invalid month: [{}]", month);
                return ResponseEntity.badRequest().build();
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<SessionMonth> getSessionMonth(@PathVariable Integer id, @PathVariable Integer year
            , @PathVariable Integer month) {
        Optional<SessionYear> sessionYear = sessionYearRepository.findByLocationIdAndSessionYear(id, year);

        if (sessionYear.isPresent()) {
            Optional<SessionMonth> sessionMonth = sessionYear.get().getSessionMonths().stream().filter(mth -> mth.getMonthName().equals(Month.of(month))).findFirst();
            if (sessionMonth.isPresent()) {
                return ResponseEntity.ok(sessionMonth.get());
            }
        }

        return ResponseEntity.notFound().build();
    }

    private SessionMonth getFilledMonth(Integer locationId, SessionMonth sessionMonth, Integer year, Integer month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.plusMonths(1).atDay(1);

        List<SessionDay> sessionDays = sessionDateRepository.saveAll(start.datesUntil(end)
                .map(dt -> SessionDay.builder().sessionMonthId(sessionMonth.getId()).sessionDate(dt).build()).collect(Collectors.toList()));

        Collection<Settings> dailyHours = settingsRepository.findByLocationIdAndSubType(locationId, SettingsSubTypeEnum.HOUR);

        sessionDays.forEach(dt -> dt.setSessions(dailyHours.stream()
                .filter(dh -> dh.getType().name().equals(dt.getSessionDate().getDayOfWeek().toString()))
                .map(dh -> Session.builder().sessionDateId(dt.getId()).sessionTime(LocalTime.parse(dh.getVal())).build()).collect(Collectors.toList())));

        return SessionMonth.builder().sessionYearId(sessionMonth.getSessionYearId()).id(sessionMonth.getId()).monthName(Month.of(month)).monthDate(start).sessionDays(sessionDays).build();
    }

}
