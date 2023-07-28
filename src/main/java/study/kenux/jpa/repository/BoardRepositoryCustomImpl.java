package study.kenux.jpa.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import study.kenux.jpa.domain.Board;
import study.kenux.jpa.domain.QMember;
import study.kenux.jpa.repository.dto.BoardSearchCond;

import java.util.List;

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
                .fetch();

        final JPAQuery<Long> countQuery = queryFactory.select(board.count())
                .from(board)
                .join(board.member, member)
                .where(betweenStartDateAndEndDate(cond), eqWriter(cond), containSearchWord(cond));

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
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
