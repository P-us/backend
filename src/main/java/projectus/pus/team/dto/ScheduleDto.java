package projectus.pus.team.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ScheduleDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private List<DayRequest> requests;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DayRequest {
        private String day;
        private List<AvailableTimesRequest> availableTimes;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AvailableTimesRequest{
        private LocalDateTime start;
        private LocalDateTime end;
    }


//    @Getter
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class Response {
//    }
}