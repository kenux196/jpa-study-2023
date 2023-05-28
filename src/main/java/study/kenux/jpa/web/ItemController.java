package study.kenux.jpa.web;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.kenux.jpa.repository.dto.ItemSearchCond;
import study.kenux.jpa.service.ItemService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<?> getItems(ItemSearchCond cond, Pageable pageable) {
        return ResponseEntity.ok(itemService.findAllItemWithPage(cond, pageable));
    }
}
