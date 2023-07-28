package study.kenux.jpa.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import study.kenux.jpa.domain.Board;
import study.kenux.jpa.domain.Member;
import study.kenux.jpa.global.config.QuerydslConfig;
import study.kenux.jpa.service.dto.BoardInfo;
import study.kenux.jpa.test.BoardDataGenerator;
import study.kenux.jpa.test.MemberDataGenerator;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DataJpaTest
@Import({QuerydslConfig.class, JdbcTemplateBoardRepository.class})
class JdbcTemplateBoardRepositoryTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private JdbcTemplateBoardRepository boardRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private BoardDataGenerator boardDataGenerator;
    private MemberDataGenerator memberDataGenerator;

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
    void testSaveWithJdbcTemplate() {
        // given
        final Member member = memberDataGenerator.getMemberList().get(0);
        final Board newBoard = Board.builder()
                .title("The new title")
                .content("The new contents")
                .createdDate(OffsetDateTime.now())
                .modifiedDate(OffsetDateTime.now())
                .member(member)
                .build();

        // when
        final Throwable throwable = catchThrowable(() -> boardRepository.save(newBoard));

        // then
        assertThat(throwable).doesNotThrowAnyException();
    }

    @Test
    void testQueryForObject() {
        final Integer count = jdbcTemplate.queryForObject("select count(*) from board", Integer.class);
        assertThat(count).isEqualTo(getBoardTotalCount());
    }

    @Test
    void testQueryForObject_withParams() {
        final Member member = memberDataGenerator.getMemberList().get(0);
        String sql = "select count(b.id) from board b " +
                "join member m on m.id = b.member_id " +
                "where m.name = ?";

        final Integer count = jdbcTemplate.queryForObject(sql, Integer.class, member.getName());

        final long expectedCount = boardDataGenerator.getBoards().stream()
                .filter(board -> board.getMember().getName().equals(member.getName()))
                .count();
        assertThat(count).isEqualTo(expectedCount);
    }

    @Test
    void testQueryForObject_RowMapper() {
        final Board board = boardDataGenerator.getBoards().get(0);
        String sql = "select b.id, b.title, b.created_date as createdDate, b.modified_date as modifiedDate, m.name as writer " +
                "from board b " +
                "join member m on m.id = b.member_id " +
                "where b.id = ?";

        final Optional<BoardInfo> boardInfo = getBoardInfo(sql, board.getId());
        assertThat(boardInfo).isPresent();
        System.out.println("boardInfo = " + boardInfo.get());
        assertThat(boardInfo.get().getTitle()).isEqualTo(board.getTitle());
    }

    private Optional<BoardInfo> getBoardInfo(String sql, Long boardId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, boardInfoRowMapper(), boardId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Test
    void testQueryMethod() {
        final Member member = memberDataGenerator.getMemberList().get(3);
        String sql = "select b.id, b.title, b.created_date as createdDate, b.modified_date as modifiedDate, m.name as writer " +
                "from board b " +
                "join member m on m.id = b.member_id " +
                "where m.name = ?";
        final List<BoardInfo> result = jdbcTemplate.query(sql, boardInfoRowMapper(), member.getName());

        final long expectedCount = boardDataGenerator.getBoards().stream()
                .filter(board -> member.getName().equals(board.getMember().getName()))
                .count();
        assertThat(result).hasSize((int) expectedCount);
    }

    @Test
    void testUsingNamedParameterJdbcTemplate() {
        final Member member = memberDataGenerator.getMemberList().get(3);
        String sql = "select b.id, b.title, b.created_date as createdDate, b.modified_date as modifiedDate, m.name as writer " +
                "from board b " +
                "join member m on m.id = b.member_id " +
                "where m.name = :name";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", member.getName());
        final List<BoardInfo> result = namedParameterJdbcTemplate.query(sql, params, boardInfoRowMapper());

        final long expectedCount = boardDataGenerator.getBoards().stream()
                .filter(board -> member.getName().equals(board.getMember().getName()))
                .count();
        assertThat(result).hasSize((int) expectedCount);
    }

    private RowMapper<BoardInfo> boardInfoRowMapper() {
        return ((rs, rowNum) -> BoardInfo.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .createdDate(rs.getObject("createdDate", OffsetDateTime.class))
                .modifiedDate(rs.getObject("modifiedDate", OffsetDateTime.class))
                .writer(rs.getString("writer"))
                .build());
    }

    private int getBoardTotalCount() {
        return boardDataGenerator.getTotalCount();
    }

}
