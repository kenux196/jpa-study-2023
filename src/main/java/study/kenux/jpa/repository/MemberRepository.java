package study.kenux.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.kenux.jpa.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
