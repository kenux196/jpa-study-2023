package study.kenux.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.kenux.jpa.domain.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
