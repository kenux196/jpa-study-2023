package study.kenux.jpa.test;

import jakarta.persistence.EntityManager;
import study.kenux.jpa.domain.Board;
import study.kenux.jpa.domain.Member;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class BoardDataGenerator extends DataGenerator {

    public static final int INIT_DATA_COUNT = 1000;

    public BoardDataGenerator(EntityManager em) {
        super(em);
    }

    @Override
    public void generate() {
        final List<Member> members = getMembers();
        final int memberCount = members.size();

        for (int i = 0; i < INIT_DATA_COUNT; i++) {
            Board board = Board.builder()
                    .title("Board Title-" + i)
                    .contents(UUID.randomUUID().toString())
                    .createdDate(OffsetDateTime.now())
                    .modifiedDate(OffsetDateTime.now())
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
