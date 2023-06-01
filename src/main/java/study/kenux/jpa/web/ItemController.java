package study.kenux.jpa.web;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.kenux.jpa.domain.Item;
import study.kenux.jpa.repository.ItemRepository;
import study.kenux.jpa.repository.dto.ItemSearchCond;
import study.kenux.jpa.service.ItemService;
import study.kenux.jpa.service.ItemServiceNoTransaction;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final ItemServiceNoTransaction itemServiceNoTransaction;
    private final ItemRepository itemRepository;

    @GetMapping
    public ResponseEntity<?> getItems(ItemSearchCond cond, Pageable pageable) {

        final Page<Item> testItems = itemRepository.findByConditionWithPage(cond, pageable);

        return ResponseEntity.ok(itemService.findAllItemWithPage(cond, pageable));
    }

    @GetMapping("/{itemId}")
    @Transactional
    public ResponseEntity<?> getItem(@PathVariable Long itemId) {
        log.info("start---------------------------");
        final Optional<Item> item = itemRepository.findById(itemId);
        log.info("find using repository directly -------------------------------- item = {} {}", item, item.get().getName());
        item.get().changeItemName("changed");
//        final Item serviceItem = itemService.getItem(itemId);
        final Optional<Item> item2 = itemRepository.findById(itemId);
        log.info("find item with service layer ------------------------ item2 = {} {}", item2, item2.get().getName());

        return ResponseEntity.ok("Ok");
    }

    @GetMapping("/test1")
    public ResponseEntity<?> testTransaction() {
        itemService.test();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test2")
    public ResponseEntity<?> testNoTransaction() {
        itemServiceNoTransaction.test();
        return ResponseEntity.ok().build();
    }
}
