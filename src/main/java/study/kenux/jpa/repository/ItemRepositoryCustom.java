package study.kenux.jpa.repository;

import study.kenux.jpa.domain.Item;
import study.kenux.jpa.repository.dto.ItemSearchCond;

import java.util.List;

public interface ItemRepositoryCustom {

    List<Item> findByCondition(ItemSearchCond cond);
}
