package study.kenux.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.kenux.jpa.domain.Board;
import study.kenux.jpa.repository.dto.BoardSearchCond;

public interface BoardRepositoryCustom {

    Page<Board> findBoardByCondition(BoardSearchCond cond, Pageable pageable);
}
