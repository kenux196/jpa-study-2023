package study.kenux.jpa.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.kenux.jpa.domain.Item;
import study.kenux.jpa.repository.ItemRepository;
import study.kenux.jpa.repository.dto.ItemDto;
import study.kenux.jpa.repository.dto.ItemSearchCond;

@Service
//@Transactional(readOnly = true)
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        log.info("Create ItemService <- {} ", itemRepository.getClass());
        this.itemRepository = itemRepository;
    }

    @Transactional
    public void save(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void saveAll(Item item1, Item item2) {
        itemRepository.save(item1);
        itemRepository.save(item2);
    }

    public Page<ItemDto> findAllItemWithPage(ItemSearchCond cond, Pageable pageable) {
        final Page<Item> items = itemRepository.findByConditionWithPage(cond, pageable);
        return items.map(ItemDto::from);
    }

    public Item getItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Not exist itemId = " + itemId));
    }
}
