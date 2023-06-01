package study.kenux.jpa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.kenux.jpa.domain.Item;
import study.kenux.jpa.domain.Member;
import study.kenux.jpa.repository.ItemRepository;
import study.kenux.jpa.repository.MemberRepository;
import study.kenux.jpa.repository.dto.ItemDto;
import study.kenux.jpa.repository.dto.ItemSearchCond;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceNoTransaction {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;


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

    public void test() {
        final List<Item> items = itemRepository.findAll();
        final List<Member> members = memberRepository.findAll();
    }
}
