package study.kenux.jpa.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationContextFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.kenux.jpa.domain.Member;
import study.kenux.jpa.domain.Team;
import study.kenux.jpa.repository.MemberRepository;
import study.kenux.jpa.repository.TeamRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;


    @BeforeAll
    void beforeAll() {
        final InitData initData = new InitData(memberRepository, teamRepository);
        initData.initTestData();
    }

    @Test
    void validateData() {
        final List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(InitData.MEMBER_COUNT);
    }
    
    public static class InitData {

        public static final int TEAM_COUNT = 3;
        public static final int MEMBER_COUNT = 10;

        private final MemberRepository memberRepository;
        private final TeamRepository teamRepository;

        public InitData(MemberRepository memberRepository, TeamRepository teamRepository) {
            this.memberRepository = memberRepository;
            this.teamRepository = teamRepository;
        }

        @Transactional
        public void initTestData() {
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
                final Member member = new Member("member" + i, i + 10);
                member.changeTeam(team);
                memberList.add(member);
            }
            memberRepository.saveAll(memberList);
        }
    }
}