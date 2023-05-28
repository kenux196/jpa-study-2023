package study.kenux.jpa.test;

import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import study.kenux.jpa.domain.Store;

import java.util.Arrays;
import java.util.List;

public class StoreGenerator {

    private final EntityManager em;

    private List<Store> storeList = Arrays.asList(
            new Store("Candy Store", "Seoul"),
            new Store("Apple Store", "Seoul"),
            new Store("Fancy Store", "Seoul")
    );

    public StoreGenerator(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void generate() {
        storeList.forEach(em::persist);
        em.flush();
        em.clear();
    }

}
