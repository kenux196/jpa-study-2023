package study.kenux.jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import study.kenux.jpa.domain.Board;
import study.kenux.jpa.test.BoardDataGenerator;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class BoardRepositoryTest extends JpaRepositoryTest {

    @Autowired
    private BoardRepository repository;

    @Test
    void findAll() {
        final List<Board> boardList = repository.findAll();
        assertThat(boardList).hasSize(BoardDataGenerator.INIT_DATA_COUNT);
        System.out.println("boardList.get(0) = " + boardList.get(0));
    }
}
