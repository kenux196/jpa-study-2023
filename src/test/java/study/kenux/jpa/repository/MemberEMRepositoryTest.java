package study.kenux.jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import study.kenux.jpa.domain.Member;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Slf4j
class MemberEMRepositoryTest {

    @PersistenceContext
    EntityManager em;


    @Test
    void memberSave() {
        log.info("start-------------");
        final Member member = new Member("member1", 11);
        log.info("save --------------");
        em.persist(member);

        assertThat(member.getId()).isNotNull();
        log.info("member = {}", member);
    }
}