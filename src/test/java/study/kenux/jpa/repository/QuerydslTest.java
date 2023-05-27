package study.kenux.jpa.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import study.kenux.jpa.config.QuerydslConfig;
import study.kenux.jpa.domain.Item;
import study.kenux.jpa.domain.QItem;
import study.kenux.jpa.domain.QStore;
import study.kenux.jpa.domain.Store;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static study.kenux.jpa.domain.QItem.item;
import static study.kenux.jpa.domain.QStore.store;

@DataJpaTest
@Import(QuerydslConfig.class)
class QuerydslTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private JPAQueryFactory queryFactory;

    List<Item> itemList = Arrays.asList(
        new Item("Mac Mini", 1000),
        new Item("MacBook Air", 2000),
        new Item("MacBook Pro 14", 3000),
        new Item("MacBook Pro 16", 4000),
        new Item("iPad Air", 900)
    );

    @BeforeEach
    void init() {
        Store store = Store.builder()
                .name("Apple Store")
                .code(UUID.randomUUID().toString())
                .build();
        em.persist(store);

        itemList.forEach(item -> {
            item.setStore(store);
            em.persist(item);
        });

        em.flush();
        em.clear();
    }

    @Test
    void findAllItemTest() {
        final List<Item> items = queryFactory.select(item)
                .from(item)
                .fetch();
        assertThat(items).hasSize(itemList.size());
    }

    @Test
    void findAllItemJoinStore() {
        final List<Item> items = queryFactory.select(item).from(item)
                .join(item.store, store)
                .fetch();
        assertThat(items).hasSize(itemList.size());
        System.out.println("items = " + items);
    }

    @Test
    void findAllItemFetchJoinStore() {
        final List<Item> items = queryFactory.select(item).from(item)
                .join(item.store, store).fetchJoin()
                .fetch();
        assertThat(items).hasSize(itemList.size());
        System.out.println("items = " + items);
    }
}
