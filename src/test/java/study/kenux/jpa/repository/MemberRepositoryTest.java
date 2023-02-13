package study.kenux.jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.BatchSize;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.kenux.jpa.domain.Member;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
//@SpringBootTest
@Slf4j
class MemberRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;


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
        final List<Member> memberList = createMemberList(1000);
        memberRepository.saveAll(memberList);
        final Instant end = Instant.now();
        log.info("time = {}", System.currentTimeMillis() - start);
    }

    private List<Member> createMemberList(int count) {
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final Member member = new Member("member" + i, i + 10);
            members.add(member);
        }
        return members;
    }
}