package study.kenux.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.kenux.jpa.repository.BoardRepository;
import study.kenux.jpa.repository.JdbcTemplateBoardRepository;
import study.kenux.jpa.service.dto.BoardInfo;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final JdbcTemplateBoardRepository jdbcTemplateBoardRepository;

    public List<BoardInfo> getBoards() {
        return jdbcTemplateBoardRepository.findAllBoard();
    }

    public Page<BoardInfo> getBoards(Pageable pageable) {
        return boardRepository.findAll(pageable).map(BoardInfo::from);
    }
}
