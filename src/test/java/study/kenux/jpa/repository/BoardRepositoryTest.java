package study.kenux.jpa.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import study.kenux.jpa.domain.Board;
import study.kenux.jpa.domain.Member;
import study.kenux.jpa.global.config.QuerydslConfig;
import study.kenux.jpa.repository.dto.BoardSearchCond;
import study.kenux.jpa.test.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import(QuerydslConfig.class)
class BoardRepositoryTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private BoardRepository boardRepository;

    private BoardDataGenerator boardDataGenerator;
    private MemberDataGenerator memberDataGenerator;

    private final int page = 0;
    private final int size = 10;

    @BeforeEach
    void setup() {
        memberDataGenerator = new MemberDataGenerator(em);
        memberDataGenerator.generate();
        boardDataGenerator = new BoardDataGenerator(em);
        boardDataGenerator.generate(memberDataGenerator.getMemberList());
        em.flush();
        em.clear();
    }

    @Test
    void testFindAll() {
        final List<Board> boardList = boardRepository.findAll();
        assertThat(boardList).hasSize(getBoardDataCount());
        System.out.println("boardList.get(0) = " + boardList.get(0));
    }

    @Test
    void testFindAllWithPage() {
        final PageRequest pageable = PageRequest.of(page, size);
        final Page<Board> result = getPagedBoard(pageable);

        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalPages()).isEqualTo(getBoardDataCount() / size);
        assertThat(result.getTotalElements()).isEqualTo(getBoardDataCount());

        System.out.println("result = " + result);
        display(result.getContent());
    }

    @Test
    void testFindAllWithPageAndSort() {
        Sort sort = Sort.by(
                Sort.Order.asc("member"),
                Sort.Order.asc("createdDate"),
                Sort.Order.desc("title")
        );
        final PageRequest pageable = PageRequest.of(page, size, sort);
        final Page<Board> result = getPagedBoard(pageable);

        System.out.println("result = " + result);
        display(result.getContent());
    }

    private Page<Board> getPagedBoard(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    private void display(List<Board> boards) {
        boards.forEach(board -> System.out.println("board = " + board));
    }

    private int getBoardDataCount() {
        return boardDataGenerator.getBoards().size();
    }

    @Test
    void testFindAllWithCondition() {
        final Member member = memberDataGenerator.getMemberList().get(0);
        BoardSearchCond cond = new BoardSearchCond();
        cond.setWriter(member.getName());

        final Sort sort = Sort.by(Sort.Order.desc("createdTime"));
        final PageRequest pageRequest = PageRequest.of(0, 10, sort);

        final Page<Board> result = boardRepository.findBoardByCondition(cond, pageRequest);

        final long count = boardDataGenerator.getBoards().stream()
                .filter(board -> member.getName().equals(board.getMember().getName()))
                .count();

        assertThat(result.getTotalPages()).isEqualTo(count / 10);
    }
}
