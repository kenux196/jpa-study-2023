package study.kenux.jpa.test;

import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import study.kenux.jpa.domain.Team;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class TeamDataGenerator {

    private final EntityManager em;
    private final List<Team> teamList = Arrays.asList(
            new Team("TeamA"),
            new Team("TeamB"),
            new Team("TeamC")
    );

    @Transactional
    public void generate() {
        teamList.forEach(em::persist);
        em.flush();
        em.clear();
    }
}
