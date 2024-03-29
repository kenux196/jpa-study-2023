package study.kenux.jpa.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import study.kenux.jpa.domain.Item;
import study.kenux.jpa.domain.Store;
import study.kenux.jpa.global.config.QuerydslConfig;
import study.kenux.jpa.test.*;

import java.util.List;

@DataJpaTest
@Import({QuerydslConfig.class})
class JpaRepositoryTest {

    @Autowired
    EntityManager em;

    @BeforeEach
    void setup() {
        final TeamDataGenerator teamDataGenerator = new TeamDataGenerator(em);
        teamDataGenerator.generate();
        final MemberDataGenerator memberDataGenerator = new MemberDataGenerator(em);
        memberDataGenerator.generate();
        final StoreDataGenerator storeDataGenerator = new StoreDataGenerator(em);
        storeDataGenerator.generate();
        final ItemDataGenerator itemDataGenerator = new ItemDataGenerator(em);
        itemDataGenerator.generate(storeDataGenerator.getStoreList());
        final BoardDataGenerator boardDataGenerator = new BoardDataGenerator(em);
        boardDataGenerator.generate(memberDataGenerator.getMemberList());
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("OneToMany: N+1 이슈")
    void test_oneToMany() {
        String jpql = "select s from Store s " +
                "left join s.items";
        final List<Store> storeList = em.createQuery(jpql, Store.class)
                .getResultList();

        for (Store store : storeList) {
            System.out.println("store = " + store);
            for (Item item : store.getItems()) {
                System.out.println("    item = " + item);
            }
        }
    }

    @Test
    @DisplayName("OneToMany: fetch join")
    void test_OneToMany_FetchJoin() {
        String jpql = "select s from Store s " +
                "join fetch s.items";
        final List<Store> storeList = em.createQuery(jpql, Store.class)
                .getResultList();

        for (Store store : storeList) {
            System.out.println("store = " + store);
            for (Item item : store.getItems()) {
                System.out.println("    item = " + item);
            }
        }
    }

    @Test
    @DisplayName("ManyToOne: normal join")
    void test_ManyToOne_InnerJoin() {
        String jpql = "select i from Item i " +
                "join i.store";
        final List<Item> resultList = em.createQuery(jpql, Item.class)
                .getResultList();

        for (Item item : resultList) {
            System.out.println("item = " + item);
        }
    }

    @Test
    @DisplayName("ManyToOne: fetch join")
    void test_ManyToOne_FetchJoin() {
        String jpql = "select i from Item i " +
                "join fetch i.store";
        final List<Item> resultList = em.createQuery(jpql, Item.class)
                .getResultList();
        System.out.println("resultList = " + resultList);
        for (Item item : resultList) {
            System.out.println("item = " + item);
        }
    }

}
