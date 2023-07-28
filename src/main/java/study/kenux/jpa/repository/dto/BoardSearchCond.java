package study.kenux.jpa.repository.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class BoardSearchCond {

    private String searchWord;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private String writer;
}
