package study.kenux.jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import study.kenux.jpa.domain.Board;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static study.kenux.jpa.test.BoardDataGenerator.*;


class BoardRepositoryTest extends JpaRepositoryTest {

    @Autowired
    private BoardRepository repository;

    private final int page = 0;
    private final int size = 10;

    @Test
    void testFindAll() {
        final List<Board> boardList = repository.findAll();
        assertThat(boardList).hasSize(BOARD_TEST_DATA_COUNT);
        System.out.println("boardList.get(0) = " + boardList.get(0));
    }

    @Test
    void testFindAllWithPage() {
        final PageRequest pageable = PageRequest.of(page, size);
        final Page<Board> result = getPagedBoard(pageable);

        assertThat(result.getSize()).isEqualTo(10);
        assertThat(result.getTotalPages()).isEqualTo(BOARD_TEST_DATA_COUNT / size);
        assertThat(result.getTotalElements()).isEqualTo(BOARD_TEST_DATA_COUNT);

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
        return repository.findAll(pageable);
    }

    private void display(List<Board> boards) {
        boards.forEach(board -> System.out.println("board = " + board));
    }
}
