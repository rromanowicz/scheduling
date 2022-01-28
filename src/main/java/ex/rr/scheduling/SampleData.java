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

    @Autowired
    private CalendarEntryRepository calendarEntryRepository;

    @Autowired
    private HostRepository hostRepository;


    public void createSampleData() {

        List<UserEntity> userEntities = new ArrayList<>();
        IntStream.range(1, 4).forEach(it -> {
            userEntities.add(UserEntity.builder().username("User" + it).email("user" + it + "@ex.rr").role(RoleEnum.USER).build());
        });
        userEntities.add(UserEntity.builder().username("Host_1").email("Host_1@ex.rr").role(RoleEnum.HOST).build());
        userRepository.saveAll(userEntities);

        List<SettingsEntryEntity> settingsEntities = new ArrayList<>();
        IntStream.range(1, 6).forEach(it -> {
            Stream.of("16:00:00", "17:30:00", "19:00:00").forEach(str -> {
                settingsEntities.add(SettingsEntryEntity.builder().type("day").subType("" + it).val(str).build());
            });
        });
        Stream.of("10:00:00", "11:30:00").forEach(str -> {
            settingsEntities.add(SettingsEntryEntity.builder().type("day").subType("6").val(str).build());
        });

        List<CalendarEntryEntity> calendarEntries = new ArrayList<>();
        List<SettingsEntryEntity> settingsDays = settingsEntities.stream().filter(settingsEntryEntity -> "day".equals(settingsEntryEntity.getType())).collect(Collectors.toList());
        LocalDate startDate = LocalDate.parse("2022-01-01");


        for (int i = 1; i <= 31; i++) {
            int dayOfWeek = startDate.getDayOfWeek().getValue();

            CalendarEntryEntity calendar = CalendarEntryEntity.builder().sessionDate(startDate).build();

            List<SettingsEntryEntity> dayHours = settingsDays.stream().filter(settingsEntryEntity -> settingsEntryEntity.getSubType().equals(String.valueOf(dayOfWeek))).collect(Collectors.toList());
            dayHours.forEach(it -> {
                HourEntity hourEntity = HourEntity.builder().sessionTime(LocalTime.parse(it.getVal())).count(0).build();
                calendar.addHour(hourEntity);
            });

            calendarEntries.add(calendar);
            startDate = startDate.plusDays(1L);
        }


        HostEntity host = HostEntity.builder()
                .host(userRepository.findByUsername("Host_1"))
                .location(
                        Collections.singletonList(LocationEntity.builder()
                                .calendar(CalendarEntity.builder().calendarEntries(calendarEntries).build())
                                .locationDetails(LocationDetailsEntity.builder()
                                        .city("City")
                                        .address("Address")
                                        .postCode("PostCode")
                                        .description("Description")
                                        .build())
                                .settings(SettingsEntity.builder().settingsEntries(settingsEntities).build())
                                .build())
                )
                .description("Host_1 Description").build();

        hostRepository.saveAndFlush(host);


        Optional<CalendarEntity> calendar = calendarRepository.findById(1);

        CalendarEntity calendarEntity = calendar.get();

        calendarEntity.getCalendarEntries().get(0).getHours().get(0).addUser(userRepository.findById(1).get());

//        calendarEntity.getCalendarEntries().forEach(ce -> {
//            ce.getHours().forEach(hr -> {
//                if (hr.getUsers().isEmpty()) {
//                    hr.getUsers().add(userRepository.findById(getRandomNumberUsingNextInt(1, 3)).get());
//                }
//            });
//        });

//        calendarEntryRepository.saveAll(calendarEntity.getCalendarEntries());

        calendarRepository.save(calendar.get());

    }

}

