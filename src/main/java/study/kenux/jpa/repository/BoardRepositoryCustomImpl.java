package study.kenux.jpa.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import study.kenux.jpa.domain.Board;
import study.kenux.jpa.domain.QMember;
import study.kenux.jpa.repository.dto.BoardSearchCond;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.core.types.OrderSpecifier.*;
import static study.kenux.jpa.domain.QBoard.board;
import static study.kenux.jpa.domain.QMember.*;

@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Board> findBoardByCondition(BoardSearchCond cond, Pageable pageable) {

        final List<Board> contents = queryFactory.select(board)
                .from(board)
                .join(board.member, member).fetchJoin()
                .where(betweenStartDateAndEndDate(cond), eqWriter(cond), containSearchWord(cond))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getBoardSort(pageable).toArray(OrderSpecifier[]::new))
                .fetch();

        final JPAQuery<Long> countQuery = queryFactory.select(board.count())
                .from(board)
                .join(board.member, member)
                .where(betweenStartDateAndEndDate(cond), eqWriter(cond), containSearchWord(cond));

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    private List<OrderSpecifier<?>> getBoardSort(Pageable pageable) {
        final List<OrderSpecifier<?>> orders = new ArrayList<>();
        final Sort sort = pageable.getSort();

        if (sort.isEmpty()) {
            return orders;
        }
        sort.stream().forEach(order -> {
            final Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();
            switch (property) {
                case "id" -> orders.add(new OrderSpecifier<>(direction, board.id));
                case "title" -> orders.add(new OrderSpecifier<>(direction, board.title));
                case "createdDate" -> orders.add(new OrderSpecifier<>(direction, board.createdDate));
                case "modifiedDate" -> orders.add(new OrderSpecifier<>(direction, board.modifiedDate));
                case "writer" -> orders.add(new OrderSpecifier<>(direction, board.member.name));
                default -> throw new IllegalStateException("Unexpected value: " + property);
            }
        });
        return orders;
    }

    private BooleanExpression betweenStartDateAndEndDate(BoardSearchCond cond) {
        if (cond.getStartDate() == null && cond.getEndDate() == null) {
            return null;
        }
        return board.createdDate.between(cond.getStartDate(), cond.getEndDate());
    }

    private BooleanExpression containSearchWord(BoardSearchCond cond) {
        if (StringUtils.hasText(cond.getSearchWord())) {
            return board.title.contains(cond.getSearchWord())
                    .or(board.content.contains(cond.getSearchWord()));
        }
        return null;
    }

    private BooleanExpression eqWriter(BoardSearchCond cond) {
        if (StringUtils.hasText(cond.getWriter())) {
            return board.member.name.eq(cond.getWriter());
        }
        return null;
    }
}
