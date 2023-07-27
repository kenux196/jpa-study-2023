package study.kenux.jpa.service.dto;

import lombok.Builder;
import lombok.Data;
import study.kenux.jpa.domain.Board;

import java.time.OffsetDateTime;

@Data
@Builder
public class BoardInfo {

    private Long id;
    private String title;
    private String writer;
    private OffsetDateTime createdDate;
    private OffsetDateTime modifiedDate;

    public static BoardInfo from(Board board) {
        return BoardInfo.builder()
                .id(board.getId())
                .title(board.getTitle())
                .writer(board.getMember().getName())
                .createdDate(board.getCreatedDate())
                .modifiedDate(board.getModifiedDate())
                .build();
    }
}
