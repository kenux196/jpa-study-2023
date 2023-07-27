package study.kenux.jpa.test;

import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import study.kenux.jpa.domain.Board;
import study.kenux.jpa.domain.Member;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class BoardDataGenerator {
    private static final int BOARD_TEST_DATA_COUNT = 1000;
    private static final OffsetDateTime BASE_DATE_TIME = OffsetDateTime.of(
            2023, 1, 1, 1, 1, 1, 0, ZoneOffset.UTC);

    private final EntityManager em;
    private final List<Board> boards = new ArrayList<>();

    public void generate(List<Member> members) {
        final int memberCount = members.size();
        for (int i = 0; i < BOARD_TEST_DATA_COUNT; i++) {
            Board board = Board.builder()
                    .title("Board Title-" + i)
                    .content(UUID.randomUUID().toString())
                    .createdDate(BASE_DATE_TIME.plusHours(i))
                    .modifiedDate(BASE_DATE_TIME.plusHours(i + 1L))
                    .member(members.get(i % memberCount))
                    .build();
            em.persist(board);
            boards.add(board);
        }
        em.flush();
        em.clear();
    }

    public int getTotalCount() {
        return boards.size();
    }
}
