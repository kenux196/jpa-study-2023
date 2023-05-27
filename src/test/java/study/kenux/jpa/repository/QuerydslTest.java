package study.kenux.jpa.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import study.kenux.jpa.config.QuerydslConfig;
import study.kenux.jpa.domain.Item;
import study.kenux.jpa.domain.Store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
        Sort sort = Sort.by(orderPrice, orderName);
        final Pageable pageable = PageRequest.of(0, 10, sort);

        final List<Item> items = queryFactory.select(item).from(item)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(multiSortItem(pageable))
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

    private OrderSpecifier<?>[] multiSortItem(Pageable pageable) {
        final Sort sort = pageable.getSort();
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        sort.forEach(order -> {
            final Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
            switch (order.getProperty()) {
                case "name" -> {
                    orderSpecifiers.add(new OrderSpecifier<>(direction, item.name));
                }
                case "price" -> {
                    orderSpecifiers.add(new OrderSpecifier<>(direction, item.price));
                }
            }
        });

        return orderSpecifiers.toArray(OrderSpecifier[]::new);
    }

    @Test
    void caseExpressionTest() {
        final StringExpression cases = new CaseBuilder()
                .when(item.price.gt(2000)).then("expensive")
                .otherwise("free");

        final List<String> result = queryFactory.select(cases)
                .from(item)
                .fetch();

        final long freeCount = result.stream()
                .filter(value -> value.equals("free"))
                .count();
        assertThat(freeCount).isEqualTo(3);
        System.out.println("result = " + result);

    }

    @Test
    void selectProperty() {
        final List<Tuple> result = queryFactory.select(item.name, item.price).from(item).fetch();
        System.out.println("result = " + result);
        final List<ItemDto> itemDtoList = result.stream()
                .map(tuple -> {
                    final String itemName = tuple.get(0, String.class);
                    final Integer itemPrice = tuple.get(1, Integer.class);
                    return new ItemDto(itemName, itemPrice);
                }).toList();
        assertThat(itemDtoList).hasSize(itemList.size());
    }

    @Data
    private static class ItemDto {
        private String name;
        private Integer price;

        public ItemDto(String name, Integer price) {
            this.name = name;
            this.price = price;
        }
    }


}
