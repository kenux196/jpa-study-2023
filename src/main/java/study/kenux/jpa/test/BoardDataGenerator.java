package study.kenux.jpa.test;

import jakarta.persistence.EntityManager;
import study.kenux.jpa.domain.Board;
import study.kenux.jpa.domain.Member;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

public class BoardDataGenerator extends DataGenerator {

    public static final int BOARD_TEST_DATA_COUNT = 1000;

    private static OffsetDateTime BASE_DATE_TIME = OffsetDateTime.of(
            2023, 1, 1, 1, 1, 1, 0, ZoneOffset.UTC);

    public BoardDataGenerator(EntityManager em) {
        super(em);
    }

    @Override
    public void generate() {
        final List<Member> members = getMembers();
        final int memberCount = members.size();

        for (int i = 0; i < BOARD_TEST_DATA_COUNT; i++) {
            Board board = Board.builder()
                    .title("Board Title-" + i)
                    .content(UUID.randomUUID().toString())
                    .createdDate(BASE_DATE_TIME.plusHours(i))
                    .modifiedDate(BASE_DATE_TIME.plusHours(i + 1))
                    .member(members.get(i % memberCount))
                    .build();
            em.persist(board);
        }
    }

    private List<Member> getMembers() {
        String jpql = "select m from Member m";
        return em.createQuery(jpql, Member.class).getResultList();
    }
}
