package study.kenux.jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.kenux.jpa.domain.Member;

@SpringBootTest
@Slf4j
class EntityManagerTest {

    @PersistenceContext
    private EntityManager em;


    @BeforeEach
    void beforeEach() {
        log.info("beforeEach");
        final Member member = new Member("member1", 11);
        em.persist(member);
        em.flush();
        em.clear();
    }

    @Test
    @Transactional
    void transaction_commit() {
        final Member member = new Member("member1", 11);
        em.persist(member);
    }



}