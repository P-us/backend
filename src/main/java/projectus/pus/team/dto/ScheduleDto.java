package projectus.pus.team.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import projectus.pus.team.entity.Schedule;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        private LocalTime start;
        private LocalTime end;
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String name;
        private List<DayResponse> responses;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DayResponse {
        private String day;
        private LocalTime start;
        private LocalTime end;
    }
}