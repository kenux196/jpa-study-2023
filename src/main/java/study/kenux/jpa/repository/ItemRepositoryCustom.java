package study.kenux.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.kenux.jpa.domain.Item;
import study.kenux.jpa.repository.dto.ItemSearchCond;

public interface ItemRepositoryCustom {

    Page<Item> findByConditionWithPage(ItemSearchCond cond, Pageable pageable);
}
