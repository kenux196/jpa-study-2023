package study.kenux.jpa.test;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.kenux.jpa.domain.Member;
import study.kenux.jpa.domain.Team;
import study.kenux.jpa.repository.MemberRepository;
import study.kenux.jpa.repository.TeamRepository;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitService implements ApplicationListener<ApplicationStartedEvent> {

    private final EntityManager em;

    private static final int MEMBER_COUNT = 1000;
    private static final int TEAM_COUNT = 100;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("application started. will be set up test data");
        createTestData();
    }

    public void createTestData() {
        createTeam();
        createMember();

        final StoreGenerator storeGenerator = new StoreGenerator(em);
        storeGenerator.generate();
    }

    private void createTeam() {
        for (int i = 0; i < TEAM_COUNT; i++) {
            em.persist(new Team("team" + i));
        }
        em.flush();
        em.clear();
    }

    private void createMember() {
        final List<Team> teamList =
                em.createQuery("select t from Team t", Team.class)
                        .getResultList();
        int teamSize = teamList.size();
        for (int i = 0; i < MEMBER_COUNT; i++) {
            final int num = i % teamSize;
            final Team team = teamList.get(num);
            em.persist(new Member("member" + i, i + 10, team));
        }
        em.flush();
        em.clear();
    }
}
