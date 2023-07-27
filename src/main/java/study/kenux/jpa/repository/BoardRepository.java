package study.kenux.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import study.kenux.jpa.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select b from Board b")
    Page<Board> findAllPaged(Pageable pageable);
}
