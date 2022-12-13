package projectus.pus.team.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import projectus.pus.team.entity.Team;

import java.util.List;
import java.util.stream.Collectors;

public class TeamDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String name;
    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long teamId;
        private String name;
        private String leader;
        private List<String> userName;

        public Response(Team team){
            this.teamId = team.getId();
            this.name = team.getName();
            this.leader = team.getLeader().getUserName();
            this.userName = team.getMembers()
                    .stream()
                    .map(member -> member.getUser().getUserName())
                    .collect(Collectors.toList());
        }
    }
}
