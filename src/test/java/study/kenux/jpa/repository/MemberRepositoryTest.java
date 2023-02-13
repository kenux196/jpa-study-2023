package study.kenux.jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import study.kenux.jpa.domain.Member;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
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

    public boolean isDetached(Member entity) {
        return entity.getId() != null  // must not be transient
                && !em.contains(entity)  // must not be managed now
                && em.find(Member.class, entity.getId()) != null;  // must not have been removed
    }
}