package study.kenux.jpa.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import study.kenux.jpa.config.QuerydslConfig;
import study.kenux.jpa.domain.Item;
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

    @Test
    void findItemByCondition_itemName() {
        final List<Item> macMini = queryFactory.select(item)
                .from(item)
                .where(item.name.eq("Mac Mini"))
                .fetch();
        assertThat(macMini).hasSize(1);
    }

    @Test
    void findItemWithPriceSortDesc() {
        final List<Item> items = queryFactory.select(item)
                .from(item)
                .orderBy(orderByPrice())
                .fetch();
        assertThat(items.get(0).getPrice()).isEqualTo(4000);
    }

    private OrderSpecifier<?> orderByPrice() {
         return item.price.desc();
    }

    @Test
    void findItemWithOderSpecifierAndPage() {
        Sort.Order orderName = new Sort.Order(Sort.Direction.ASC, "name");
        Sort.Order orderPrice = new Sort.Order(Sort.Direction.DESC, "price");
        Sort sort = Sort.by(orderName, orderPrice);
        final Pageable pageable = PageRequest.of(0, 10, sort);

        final List<Item> items = queryFactory.select(item).from(item)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sortItems(pageable))
                .fetch();

        assertThat(items).hasSize(itemList.size());
        System.out.println("items = " + items);
    }

    private OrderSpecifier<?> sortItems(Pageable pageable) {
        final Sort sort = pageable.getSort();

        if (!sort.isEmpty()) {
            for (Sort.Order order : sort) {
                final Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "name" -> {
                        return new OrderSpecifier<>(direction, item.name);
                    }
                    case "price" -> {
                        return new OrderSpecifier<>(direction, item.price);
                    }
                }
            }
        }

        return new OrderSpecifier<>(Order.DESC, item.name);
    }
}
