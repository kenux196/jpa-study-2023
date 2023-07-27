package study.kenux.jpa.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import study.kenux.jpa.domain.Board;
import study.kenux.jpa.domain.Member;
import study.kenux.jpa.global.config.QuerydslConfig;
import study.kenux.jpa.test.BoardDataGenerator;
import study.kenux.jpa.test.MemberDataGenerator;

import java.time.OffsetDateTime;

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

//        assertThat()
    }

    private int getBoardTotalCount() {
        return boardDataGenerator.getTotalCount();
    }

}
