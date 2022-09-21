package ex.rr.scheduling.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DateRange {

    private LocalDate dateFrom;
    private LocalDate dateTo;

    @JsonIgnore
    public List<LocalDate> getDateList() {
        return dateFrom.datesUntil(dateTo.plusDays(1L)).collect(Collectors.toList());
    }

    @JsonIgnore
    public Set<Integer> getYears() {
        return new HashSet<>(List.of(dateTo.getYear(), dateFrom.getYear()));
    }
}
