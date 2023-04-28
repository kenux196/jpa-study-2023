package study.kenux.jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.kenux.jpa.domain.Member;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class EntityManagerTest {

    @PersistenceContext
    private EntityManager em;

    @Test
    @Transactional
    void find() {
        saveMember();
//        saveMemberNotClear();
        log.info("find member");
        final Member member = em.find(Member.class, 1L);
        assertThat(member).isNotNull();
    }

    void saveMember() {
        log.info("save member = start");
        final Member member = new Member("newMember", 11);
        em.persist(member);
        log.info("call em.flush");
        em.flush();
        em.clear();
        log.info("save member = end");
    }

    void saveMemberNotClear() {
        log.info("save member = start");
        final Member member = new Member("newMember", 11);
        em.persist(member);
        log.info("call em.flush");
        em.flush();
        log.info("save member = end");
    }

}
