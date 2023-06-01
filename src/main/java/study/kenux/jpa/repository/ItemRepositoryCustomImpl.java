package study.kenux.jpa.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import study.kenux.jpa.domain.Item;
import study.kenux.jpa.repository.dto.ItemSearchCond;

import java.util.ArrayList;
import java.util.List;

import static study.kenux.jpa.domain.QItem.item;
import static study.kenux.jpa.domain.QStore.store;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Item> findByConditionWithPage(ItemSearchCond cond, Pageable pageable) {
        final JPAQuery<Item> itemJPAQuery = queryFactory.select(item)
                .from(item)
                .join(item.store, store).fetchJoin()
                .where(containItemName(cond), containStoreName(cond), rangeItemPrice(cond));

        final List<Item> items = itemJPAQuery
                .orderBy(getOrderSpecifier(pageable))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        final long count = itemJPAQuery.fetch().size();

        return new PageImpl<>(items, pageable, count);
    }

    private BooleanExpression containItemName(ItemSearchCond cond) {
        if (StringUtils.hasText(cond.getItemName())) {
            return item.name.contains(cond.getItemName());
        }

        return null;
    }

    private BooleanExpression containStoreName(ItemSearchCond cond) {
        if (StringUtils.hasText(cond.getStoreName())) {
            return item.store.name.contains(cond.getStoreName());
        }

        return null;
    }

    private BooleanExpression rangeItemPrice(ItemSearchCond cond) {
        if (cond.getMinPrice() != null && cond.getMaxPrice() != null) {
            return item.price.between(cond.getMinPrice(), cond.getMaxPrice());
        }

        if (cond.getMinPrice() != null) {
            return item.price.gt(cond.getMinPrice());
        }

        if (cond.getMaxPrice() != null) {
            return item.price.lt(cond.getMaxPrice());
        }

        return null;
    }

    private OrderSpecifier<?>[] getOrderSpecifier(Pageable pageable) {
        final Sort sort = pageable.getSort();
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        for (Sort.Order order : sort) {
            final Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();
            switch (property) {
                case "price" -> orderSpecifiers.add(new OrderSpecifier<>(direction, item.price));
                case "store" -> orderSpecifiers.add(new OrderSpecifier<>(direction, item.store.name));
                default -> orderSpecifiers.add(new OrderSpecifier<>(direction, item.name));
            }
        }
        return orderSpecifiers.toArray(OrderSpecifier[]::new);
    }

}
