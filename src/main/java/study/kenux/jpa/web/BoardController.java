package study.kenux.jpa.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.kenux.jpa.service.BoardService;
import study.kenux.jpa.service.dto.BoardInfo;


@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<?> getBoard(Pageable pageable) {
        final Page<BoardInfo> boards = boardService.getBoards(pageable);
        return ResponseEntity.ok(boards);
    }
}
