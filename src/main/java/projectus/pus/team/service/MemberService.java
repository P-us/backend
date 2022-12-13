package projectus.pus.team.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projectus.pus.team.entity.Member;
import projectus.pus.team.entity.Team;
import projectus.pus.team.repository.MemberRepository;
import projectus.pus.user.entity.User;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
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
}
