package study.kenux.jpa.service;

import jakarta.persistence.EntityManager;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import study.kenux.jpa.domain.Member;
import study.kenux.jpa.domain.Team;
import study.kenux.jpa.repository.MemberRepository;
import study.kenux.jpa.repository.TeamRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    EntityManager em;


    @BeforeEach
    void beforeEach() {
        final InitData initData = new InitData(memberRepository, teamRepository, em);
        initData.initTestData();
    }

    @Test
    void validateData() {
        final List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(InitData.MEMBER_COUNT);
    }

    @Test
    void jpql_join() {
        final List<Member> members = memberRepository.findAllJoin();
        assertThat(members).hasSize(InitData.MEMBER_COUNT);
    }

    @Test
    void jpql_fetchJoin() {
        final List<Member> allFetchJoin = memberRepository.findAllFetchJoin();
        assertThat(allFetchJoin).hasSize(InitData.MEMBER_COUNT);
    }

    @Test
    void findTeam1() {
        final List<Team> teamList = teamRepository.findAll();
        System.out.println("teamList.size() = " + teamList.size());
    }

    @Test
    void findTeam2() {
        final List<Team> teams = teamRepository.findAllWithJPQL();
        System.out.println("teams.size() = " + teams.size());
    }

    @Test
    void findTeam3() {
        final List<Team> teams = teamRepository.findAllWithJPQL_fetchJoin();
        System.out.println("teams.size() = " + teams.size());
    }

    @Test
    void findTeam4() {
        final List<Team> teams = teamRepository.findAll3();
        System.out.println("teams.size() = " + teams.size());
    }

    @Test
    @Transactional
    void findTeamWithMember() {
        String query = "select t from Team t " +
                "join fetch t.members m ";
        final List<Team> resultList = em.createQuery(query, Team.class)
                .getResultList();
        System.out.println("resultList = " + resultList);
        em.clear();

        // fetch join 을 페이징하므로 다음의 경고가 로그에 나온다.
        // firstResult/maxResults specified with collection fetch; applying in memory => 안티패턴이다.
        final List<Team> pagedResult = em.createQuery(query, Team.class)
                .setFirstResult(0)
                .setMaxResults(10)
                .getResultList();
        System.out.println("pagedResult = " + pagedResult);
        em.clear();

        String query2 = "select t from Team t";

        final List<Team> resultList1 = em.createQuery(query2, Team.class)
                .getResultList();
        for (Team team : resultList1) {
            System.out.println("team.getMembers() = " + team.getMembers());
        }
        em.clear();

        final List<Team> pagedResult1 = em.createQuery(query2, Team.class)
                .setFirstResult(1)
                .setMaxResults(10)
                .getResultList();
        System.out.println("pagedResult = " + pagedResult1);
        em.clear();
    }

    @Test
    @Transactional
    void using_batch_size_paging() {
        em.clear();
        String query2 = "select t from Team t";
        final List<Team> pagedResult1 = em.createQuery(query2, Team.class)
                .setFirstResult(1)
                .setMaxResults(10)
                .getResultList();
        System.out.println("pagedResult = " + pagedResult1);
        for (Team team : pagedResult1) {
            for (Member member : team.getMembers()) {
                System.out.println("member = " + member.getName());
            }
        }
    }

    @Test
    @Transactional
    void getMembersWithPage() {
        final Pageable pageable = PageRequest.of(1, 10);
        final Page<Member> members = memberService.getMembers(pageable);

        final List<MemberDto> memberDtos = members.stream()
                .map(member -> MemberDto.builder()
                        .id(member.getId())
                        .name(member.getName())
                        .age(member.getAge())
                        .teamName(member.getTeam().getName())
                        .build())
                .toList();
        Page<MemberDto> result = new PageImpl<>(memberDtos, pageable, members.getTotalElements());
        System.out.println("result = " + result);
    }

    @Data
    @Builder
    public static class MemberDto {
        Long id;
        String name;
        Integer age;
        String teamName;
    }

    public class InitData {

        public static final int TEAM_COUNT = 1000;
        public static final int MEMBER_COUNT = 10000;

        private final MemberRepository memberRepository;
        private final TeamRepository teamRepository;
        private final EntityManager em;

        public InitData(MemberRepository memberRepository, TeamRepository teamRepository, EntityManager em) {
            this.memberRepository = memberRepository;
            this.teamRepository = teamRepository;
            this.em = em;
        }

//        @Transactional
        public void initTestData() {
            createTeam();
            createMember();
            em.clear();
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
}