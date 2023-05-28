package study.kenux.jpa.test;

import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import study.kenux.jpa.domain.Team;

import java.util.Arrays;
import java.util.List;

public class TeamGenerator extends DataGenerator {

    private final List<Team> teamList = Arrays.asList(
            new Team("TeamA"),
            new Team("TeamB"),
            new Team("TeamC")
    );

    public TeamGenerator(EntityManager em) {
        super(em);
    }

    @Override
    @Transactional
    public void generate() {
        teamList.forEach(em::persist);
        em.flush();
        em.clear();
    }
}
