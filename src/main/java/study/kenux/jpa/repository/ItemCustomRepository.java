package study.kenux.jpa.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.kenux.jpa.domain.Item;

import java.util.List;

import static study.kenux.jpa.domain.QItem.item;

@Repository
@RequiredArgsConstructor
public class ItemCustomRepository {

    private final JPAQueryFactory queryFactory;

    public List<Item> findAll() {
        return queryFactory.select(item)
                .from(item)
                .fetch();
    }
}
