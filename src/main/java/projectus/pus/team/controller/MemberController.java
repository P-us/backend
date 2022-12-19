package projectus.pus.team.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projectus.pus.config.security.CurrentUser;
import projectus.pus.config.security.CustomUserDetails;
import projectus.pus.team.dto.ScheduleDto;
import projectus.pus.team.service.MemberService;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> addSchedule(
            @RequestBody ScheduleDto.Request request,
            @CurrentUser CustomUserDetails currentUser){
        Long scheduleId = memberService.addSchedule(request,currentUser.getUserId());
        return ResponseEntity.created(URI.create("/api/members/"+scheduleId)).build();
    }
}
