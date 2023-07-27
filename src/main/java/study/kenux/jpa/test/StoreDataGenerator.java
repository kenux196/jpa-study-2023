package study.kenux.jpa.test;

import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import study.kenux.jpa.domain.Store;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class StoreDataGenerator {

    private final EntityManager em;
    private final List<Store> storeList = Arrays.asList(
            new Store("Candy Store", "Seoul"),
            new Store("Apple Store", "Seoul"),
            new Store("Fancy Store", "Seoul")
    );

    @Transactional
    public void generate() {
        storeList.forEach(em::persist);
        em.flush();
        em.clear();
    }
}
