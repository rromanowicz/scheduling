package ex.rr.scheduling.service;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import ex.rr.scheduling.model.CalendarEntity;
import ex.rr.scheduling.repository.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarService implements GraphQLQueryResolver, GraphQLMutationResolver {

    @Autowired
    private CalendarRepository calendarRepository;


    public List<CalendarEntity> getCalendar() {
        return calendarRepository.findAll();
    }

    public List<CalendarEntity> getCalendarByUserId(Integer userId) {
        return calendarRepository.findByHoursUsersId(userId);
    }
}
