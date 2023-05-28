package study.kenux.jpa.test;

import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import study.kenux.jpa.domain.Item;
import study.kenux.jpa.domain.Store;

import java.util.Arrays;
import java.util.List;

public class ItemGenerator extends DataGenerator {
    private final List<Item> itemList = Arrays.asList(
            new Item("Mac Mini", 1000),
            new Item("MacBook Air", 2000),
            new Item("MacBook Pro 14", 3000),
            new Item("MacBook Pro 16", 4000),
            new Item("iPad Air", 900)
    );

    public ItemGenerator(EntityManager em) {
        super(em);
    }

    @Override
    @Transactional
    public void generate() {
        final Store appleStore = em.createQuery("select s from Store s where s.name = :storeName", Store.class)
                .setParameter("storeName", "Apple Store")
                .getSingleResult();

        itemList.forEach(item -> {
            item.setStore(appleStore);
            em.persist(item);
        });
        em.flush();
        em.clear();
    }
}
