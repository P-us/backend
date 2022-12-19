package projectus.pus.team.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import projectus.pus.team.dto.ScheduleDto;
import projectus.pus.team.entity.Member;
import projectus.pus.team.entity.Schedule;
import projectus.pus.team.entity.Team;
import projectus.pus.team.repository.MemberRepository;
import projectus.pus.team.repository.ScheduleRepository;
import projectus.pus.user.entity.User;
import projectus.pus.user.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    @Transactional
    public void save(User user, Team team) {
        if(!memberRepository.existsByUserAndTeam(user, team)){
            Member member = new Member(user, team);
            member.addTeam(team);
            memberRepository.save(member);
        }
        else{
            throw new IllegalArgumentException("이미 참여중인 멤버입니다.");
        }
    }

    @Transactional
    public void out(User user, Team team){
        Member member = memberRepository.findByUserAndTeam(user, team)
                .orElseThrow(()-> new IllegalArgumentException("해당 방에 참여하지 않은 멤버입니다."));
        team.out(member);
        memberRepository.delete(member);
    }

    @Transactional
    public Long addSchedule(ScheduleDto.Request request, Long userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("계정이 존재하지 않습니다."));
        Member member = memberRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("해당 방에 참여하지 않은 멤버입니다."));
        if(!CollectionUtils.isEmpty(request.getRequests())) {
        for (ScheduleDto.DayRequest dayList : request.getRequests()){
            for(ScheduleDto.AvailableTimesRequest availableTimesRequest :dayList.getAvailableTimes()){
                Schedule schedule = Schedule
                        .builder()
                        .member(member)
                        .day(dayList.getDay())
                        .start(availableTimesRequest.getStart())
                        .end(availableTimesRequest.getEnd())
                        .build();
                scheduleRepository.save(schedule);
            }}}
        return member.getId();
    }
}
