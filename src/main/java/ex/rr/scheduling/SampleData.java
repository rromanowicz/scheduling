package ex.rr.scheduling;

import ex.rr.scheduling.model.*;
import ex.rr.scheduling.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class SampleData {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private CalendarRepository calendarRepository;


    public void createSampleData() {

        /*Generate users*/
        List<UserEntity> userEntities = new ArrayList<>();
        IntStream.range(1, 4).forEach(it -> {
            userEntities.add(UserEntity.builder().username("User" + it).email("user" + it + "@ex.rr").role(RoleEnum.USER).build());
        });
        userEntities.add(UserEntity.builder().username("Admin_1").email("Admin_1@ex.rr").role(RoleEnum.ADMIN).build());
        userRepository.saveAll(userEntities);

        /*Generate settings*/
        List<SettingsEntity> settingsEntities = new ArrayList<>();
        IntStream.range(1, 6).forEach(it -> {
            Stream.of("16:00:00", "17:30:00", "19:00:00").forEach(str -> {
                settingsEntities.add(SettingsEntity.builder().type("day").subType("" + it).val(str).build());
            });
        });
        Stream.of("10:00:00", "11:30:00").forEach(str -> {
            settingsEntities.add(SettingsEntity.builder().type("day").subType("6").val(str).build());
        });
        settingsRepository.saveAll(settingsEntities);

        /*Generate calendar*/
        List<CalendarEntity> calendarEntries = new ArrayList<>();
        List<SettingsEntity> settingsDays = settingsEntities.stream().filter(settingsEntryEntity -> "day".equals(settingsEntryEntity.getType())).collect(Collectors.toList());
        LocalDate startDate = LocalDate.parse("2022-01-01");

        for (int i = 1; i <= 31; i++) {
            int dayOfWeek = startDate.getDayOfWeek().getValue();

            CalendarEntity calendar = CalendarEntity.builder().sessionDate(startDate).build();

            List<SettingsEntity> dayHours = settingsDays.stream().filter(settingsEntryEntity -> settingsEntryEntity.getSubType().equals(String.valueOf(dayOfWeek))).collect(Collectors.toList());
            dayHours.forEach(it -> {
                HourEntity hourEntity = HourEntity.builder().sessionTime(LocalTime.parse(it.getVal())).count(0).build();
                calendar.addHour(hourEntity);
            });

            calendarEntries.add(calendar);
            startDate = startDate.plusDays(1L);
        }

        calendarRepository.saveAll(calendarEntries);

        /*Generate calendar entries*/
        List<CalendarEntity> calendar = calendarRepository.findAll();
        List<UserEntity> users = userRepository.findAll();

        calendar.forEach(cal -> {
            cal.getHours().forEach(hr -> {
                if (hr.getUsers().isEmpty()) {
                    hr.getUsers().add(users.get(getRandomNumberInRange(1, users.size()) - 1));
                }
            });
        });

        calendarRepository.saveAll(calendar);

    }


    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}