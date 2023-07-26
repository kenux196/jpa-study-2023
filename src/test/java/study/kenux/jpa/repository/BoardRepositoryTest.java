package study.kenux.jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import study.kenux.jpa.domain.Board;
import study.kenux.jpa.domain.Member;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static study.kenux.jpa.test.BoardDataGenerator.BOARD_TEST_DATA_COUNT;


@Import(BoardJdbcTemplateRepository.class)
class BoardRepositoryTest extends JpaRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BoardJdbcTemplateRepository boardJdbcTemplateRepository;

    private final int page = 0;
    private final int size = 10;

    @Test
    void testFindAll() {
        final List<Board> boardList = boardRepository.findAll();
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
        return boardRepository.findAll(pageable);
    }

    private void display(List<Board> boards) {
        boards.forEach(board -> System.out.println("board = " + board));
    }

    @Test
    void saveWithJdbcTemplate() {
        // given
        final Member member = memberRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("Member not exist"));
        final Board newBoard = Board.builder()
                .title("The new title")
                .content("The new contents")
                .createdDate(OffsetDateTime.now())
                .modifiedDate(OffsetDateTime.now())
                .member(member)
                .build();

        // when
        boardJdbcTemplateRepository.save(newBoard);
        em.flush();
        em.clear();

        final Board result = em.createQuery("select b from Board b where b.title = 'The new title'", Board.class)
                .getSingleResult();

        // then
        assertThat(result).isNotNull();
    }
}
