package study.kenux.jpa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.kenux.jpa.domain.Member;
import study.kenux.jpa.domain.Team;
import study.kenux.jpa.repository.MemberRepository;
import study.kenux.jpa.repository.TeamRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InitService implements ApplicationListener<ApplicationStartedEvent> {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    private static final int MEMBER_COUNT = 1000;
    private static final int TEAM_COUNT = 100;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("application started. will be set up test data");
        createTestData();
    }

    public void createTestData() {
        createTeam();
        createMember();
    }

    private void createTeam() {
        List<Team> teamList = new ArrayList<>();
        for (int i = 0; i < TEAM_COUNT; i++) {
            teamList.add(new Team("team" + i));
        }
        teamRepository.saveAll(teamList);
    }

    private void createMember() {
        List<Member> memberList = new ArrayList<>();
        final List<Team> teamList = teamRepository.findAll();
        int teamSize = teamList.size();
        for (int i = 0; i < MEMBER_COUNT; i++) {
            final int num = i % teamSize;
            final Team team = teamList.get(num);
            final Member member = new Member("member" + i, i + 10, team);
            memberList.add(member);
        }
        memberRepository.saveAll(memberList);
    }
}