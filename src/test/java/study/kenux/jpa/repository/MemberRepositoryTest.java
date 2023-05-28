package study.kenux.jpa.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import study.kenux.jpa.config.QuerydslConfig;
import study.kenux.jpa.domain.Member;
import study.kenux.jpa.domain.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
//@SpringBootTest
@Slf4j
class MemberRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void memberSave() {
        log.info("start-------------");
        final Member member = new Member("member1", 11);
        log.info("save --------------");
        em.persist(member);

        assertThat(member.getId()).isNotNull();
        log.info("member = {}", member);
    }

    @Test
    void saveWithJpaRepository() {
        final Member member = new Member("member1", 11);
        memberRepository.save(member);
        boolean detached = isDetached(member);
        log.info("member = {} {}", member, detached);
        assertThat(member.getId()).isNotNull();
        em.detach(member);
        detached = isDetached(member);
        log.info("member = {} {}", member, detached);
    }

    @Test
    void saveAndFlush() {
        final Member member = new Member("member1", 11);
        em.persist(member);
    }

    @Test
    void detachedEntityIsNotUpdate() {
        final Member member = new Member("member1", 11);
        memberRepository.save(member);
        em.detach(member);
        member.updateAge(12);
        log.info("call em.flush");
        em.flush();

        final Optional<Member> findMember = memberRepository.findById(member.getId());
        assertThat(findMember).isPresent();
        assertThat(findMember.get().getAge()).isEqualTo(11);
    }

    public boolean isDetached(Member entity) {
        return entity.getId() != null  // must not be transient
                && !em.contains(entity)  // must not be managed now
                && em.find(Member.class, entity.getId()) != null;  // must not have been removed
    }

    @Test
    void saveAll() {
        final long start = System.currentTimeMillis();
        final List<Member> memberList = createMemberList(10000);
        memberRepository.saveAll(memberList);
        log.info("time = {}", System.currentTimeMillis() - start);
    }

    @Test
    void bulkSaveTest() {
        final int savedSize = bulkSave();

        final List<Member> result = memberRepository.findAll();
        assertThat(result).hasSize(savedSize);
        log.info("member(0) = {}", result.get(0));
    }

    private int bulkSave() {
        final String query = "insert into member (name, age) " +
                "values (?, ?)";
        final List<Member> memberList = createMemberList(10000);
        final long start = System.currentTimeMillis();
        jdbcTemplate.batchUpdate(query, memberList, 100, (ps, argument) -> {
            ps.setString(1, argument.getName());
            ps.setInt(2, argument.getAge());
        });
        log.info("time = {}", System.currentTimeMillis() - start);
        em.clear();

        return memberList.size();
    }

    @Test
    void paging() throws JsonProcessingException {
        final int savedSize = bulkSave();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
//        final PageRequest pageRequest = PageRequest.of(0, 10);
        final Page<Member> pagedResult = memberRepository.findAll(pageable);
        assertThat(pagedResult.getContent()).hasSize(10);
        log.info("result = {}", pagedResult);
        ObjectMapper objectMapper = new ObjectMapper();
        final String json = objectMapper.writeValueAsString(pagedResult);
        log.info("result json = {}", json);
    }

    private List<Member> createMemberList(int count) {
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final Member member = new Member("member" + i, i + 10);
            members.add(member);
        }
        return members;
    }

    @Test
    void setTeamForMember() {
        final Team team = new Team("team1");
        teamRepository.save(team);
        final Member member = new Member("member1", 11);
        member.changeTeam(team);
        memberRepository.save(member);

        assertThat(member.getTeam().getId()).isEqualTo(team.getId());
    }
}
