package study.kenux.jpa.test;

import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import study.kenux.jpa.domain.Item;
import study.kenux.jpa.domain.Store;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public class ItemDataGenerator {

    private final EntityManager em;

    @Getter
    private final List<Item> itemList = Arrays.asList(
            new Item("Mac Mini", 1000),
            new Item("MacBook Air", 2000),
            new Item("MacBook Pro 14", 3000),
            new Item("MacBook Pro 16", 4000),
            new Item("iPad Air", 900)
    );

    @Transactional
    public void generate(List<Store> stores) {
        final Optional<Store> appleStore = stores.stream()
                .filter(store -> store.getName().equals("Apple Store"))
                .findFirst();

        if (appleStore.isEmpty()) {
            throw new IllegalStateException("store not founded");
        }

        itemList.forEach(item -> {
            item.setStore(appleStore.get());
            em.persist(item);
        });
        em.flush();
        em.clear();
    }

    public int getItemCount() {
        return itemList.size();
    }
}
