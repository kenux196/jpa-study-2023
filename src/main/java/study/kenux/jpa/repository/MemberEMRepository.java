package study.kenux.jpa.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.kenux.jpa.domain.Member;

@RequiredArgsConstructor
@Repository
public class MemberEMRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findMemberById(Long id) {
        return em.find(Member.class, id);
    }

}