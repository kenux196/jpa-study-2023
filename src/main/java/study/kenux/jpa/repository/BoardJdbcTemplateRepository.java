package study.kenux.jpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import study.kenux.jpa.domain.Board;
import study.kenux.jpa.service.dto.BoardInfo;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

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

    public List<BoardInfo> findAllBoard() {
        String sql = "select * from board";
        return jdbcTemplate.query(sql, (rs, rowNum) -> BoardInfo.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .createdDate(rs.getObject("created_date", OffsetDateTime.class))
                .modifiedDate(rs.getObject("modified_date", OffsetDateTime.class))
                .build());
    }
}
