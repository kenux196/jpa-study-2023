package study.kenux.jpa.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String contents;

    private OffsetDateTime createdDate;

    private OffsetDateTime modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Board(String title, String contents, OffsetDateTime createdDate, OffsetDateTime modifiedDate, Member member) {
        this.title = title;
        this.contents = contents;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.member = member;
    }
}
