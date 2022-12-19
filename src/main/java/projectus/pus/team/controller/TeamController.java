package projectus.pus.team.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projectus.pus.config.security.CurrentUser;
import projectus.pus.config.security.CustomUserDetails;
import projectus.pus.team.dto.TeamDto;
import projectus.pus.team.service.TeamService;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/teams")
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<Void> createTeam(
            @RequestBody TeamDto.Request request, @CurrentUser CustomUserDetails currentUser){
        Long teamId = teamService.createTeam(request.getName(),currentUser.getUserId());
        return ResponseEntity.created(URI.create("/api/teams/"+teamId)).build();
    }

    @GetMapping("{teamId}")
    public ResponseEntity<TeamDto.Response> getTeam(@PathVariable Long teamId){
        return ResponseEntity.ok().body(teamService.getTeam(teamId));
    }
    @GetMapping
    public ResponseEntity<Page<TeamDto.Response>> getTeamList(
            @CurrentUser CustomUserDetails currentUser,
            @PageableDefault(sort="modifiedDate",direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok().body(teamService.getTeamList(currentUser.getUserId(),pageable));
    }

    @PostMapping("{teamId}/user")
    public ResponseEntity<Void> participateTeam(@PathVariable Long teamId, @CurrentUser CustomUserDetails currentUser){
        teamService.participateTeam(currentUser.getUserId(),teamId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("{teamId}/user")
    public ResponseEntity<Void> leaveTeam(@PathVariable Long teamId, @CurrentUser CustomUserDetails currentUser){
        teamService.leaveTeam(currentUser.getUserId(),teamId);
        return ResponseEntity.ok().build();
    }
}
