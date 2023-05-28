package study.kenux.jpa.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.kenux.jpa.domain.Item;
import study.kenux.jpa.repository.dto.ItemSearchCond;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Item> findByCondition(ItemSearchCond cond) {
        return null;
    }
}
