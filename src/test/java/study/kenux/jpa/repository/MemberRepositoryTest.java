package study.kenux.jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.kenux.jpa.domain.Member;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

//@DataJpaTest
@SpringBootTest
@Slf4j
class MemberRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;


    @Test
    @Transactional
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
    void transaction_commit() {
        final EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            final Member member = new Member("member1", 11);
            em.persist(member);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            em.close();
        }
    }

    @Test
    void detachedEntityIsNotUpdate() {
        final Member member = new Member("member1", 11);
        memberRepository.save(member);
        em.detach(member);
        member.updateAge(12);
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
}