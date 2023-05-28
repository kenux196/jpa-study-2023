package study.kenux.jpa.test;

import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import study.kenux.jpa.domain.Store;

import java.util.Arrays;
import java.util.List;

public class StoreGenerator extends DataGenerator {

    private final List<Store> storeList = Arrays.asList(
            new Store("Candy Store", "Seoul"),
            new Store("Apple Store", "Seoul"),
            new Store("Fancy Store", "Seoul")
    );

    public StoreGenerator(EntityManager em) {
        super(em);
    }

    @Override
    @Transactional
    public void generate() {
        storeList.forEach(em::persist);
        em.flush();
        em.clear();
    }

}
