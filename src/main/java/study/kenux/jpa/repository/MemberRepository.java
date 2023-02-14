package study.kenux.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import study.kenux.jpa.domain.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "select m from Member m " +
            "join m.team")
    List<Member> findAllJoin();

    @Query(value = "select m from Member m " +
            "join fetch m.team")
    List<Member> findAllFetchJoin();
}
