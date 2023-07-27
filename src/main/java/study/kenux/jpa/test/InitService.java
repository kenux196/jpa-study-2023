package study.kenux.jpa.test;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitService implements ApplicationListener<ApplicationStartedEvent> {

    private final EntityManager em;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("application started. will be set up test data");
        createTestData();
        em.flush();
        em.clear();
    }

    public void createTestData() {
        final TeamDataGenerator teamDataGenerator = new TeamDataGenerator(em);
        teamDataGenerator.generate();

        final MemberDataGenerator memberDataGenerator = new MemberDataGenerator(em);
        memberDataGenerator.generate(teamDataGenerator.getTeamList());

        final StoreDataGenerator storeDataGenerator = new StoreDataGenerator(em);
        storeDataGenerator.generate();

        final ItemDataGenerator itemDataGenerator = new ItemDataGenerator(em);
        itemDataGenerator.generate(storeDataGenerator.getStoreList());

        final BoardDataGenerator boardDataGenerator = new BoardDataGenerator(em);
        boardDataGenerator.generate(memberDataGenerator.getMemberList());
    }
}
