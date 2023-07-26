package study.kenux.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.kenux.jpa.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
