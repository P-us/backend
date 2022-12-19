package projectus.pus.team.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectus.pus.team.dto.TeamDto;
import projectus.pus.team.entity.Team;
import projectus.pus.team.repository.TeamRepository;
import projectus.pus.user.entity.User;
import projectus.pus.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final MemberService memberService;
    private final UserRepository userRepository;
    @Transactional
    public Long createTeam(String name, Long userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다."));
        Team team = new Team(name, user);
        Long teamId = teamRepository.save(team).getId();
        memberService.save(user,team);
        return teamId;

    }
    @Transactional(readOnly = true)
    public TeamDto.Response getTeam(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
        return new TeamDto.Response(team);
    }

    @Transactional(readOnly = true)
    public Page<TeamDto.Response> getTeamList(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다."));
        List<Team> teamList = teamRepository.findByUserId(userId);
        List<TeamDto.Response> collect = teamList
                .stream()
                .map(TeamDto.Response::new)
                .collect(Collectors.toList());
        int start = (int)pageable.getOffset();
        int end = Math.min((start+pageable.getPageSize()),collect.size());
        return new PageImpl<>(collect.subList(start, end), pageable, collect.size());
    }

    @Transactional
    public void participateTeam(Long userId, Long teamId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다."));
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
        memberService.save(user,team);
    }
    @Transactional
    public void leaveTeam(Long userId, Long teamId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다."));
        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
        memberService.out(user,team);
    }
}
