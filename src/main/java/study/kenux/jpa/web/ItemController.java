package study.kenux.jpa.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.kenux.jpa.domain.Item;
import study.kenux.jpa.repository.ItemRepository;
import study.kenux.jpa.repository.dto.ItemSearchCond;
import study.kenux.jpa.service.ItemService;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;

    @GetMapping
    public ResponseEntity<?> getItems(ItemSearchCond cond, Pageable pageable) {

        final Page<Item> testItems = itemRepository.findByConditionWithPage(cond, pageable);

        return ResponseEntity.ok(itemService.findAllItemWithPage(cond, pageable));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<?> getItem(@PathVariable Long itemId) {
        final Optional<Item> item = itemRepository.findById(itemId);

        final Item serviceItem = itemService.getItem(itemId);

        return ResponseEntity.ok("Ok");
    }
}
