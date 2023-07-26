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
        final TeamGenerator teamGenerator = new TeamGenerator(em);
        teamGenerator.generate();

        final MemberGenerator memberGenerator = new MemberGenerator(em);
        memberGenerator.generate();

        final StoreGenerator storeGenerator = new StoreGenerator(em);
        storeGenerator.generate();

        final ItemGenerator itemGenerator = new ItemGenerator(em);
        itemGenerator.generate();

        final BoardDataGenerator boardDataGenerator = new BoardDataGenerator(em);
        boardDataGenerator.generate();

    }
}
