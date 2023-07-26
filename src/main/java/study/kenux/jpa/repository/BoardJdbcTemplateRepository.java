package study.kenux.jpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import study.kenux.jpa.domain.Board;

@Repository
@RequiredArgsConstructor
public class BoardJdbcTemplateRepository {
    private final JdbcTemplate jdbcTemplate;

    public void save(Board board) {
        String sql = "insert into board (title, content, created_date, modified_date, member_id) " +
                "values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                board.getTitle(),
                board.getContent(),
                board.getCreatedDate(),
                board.getModifiedDate(),
                board.getMember().getId());
    }
}
