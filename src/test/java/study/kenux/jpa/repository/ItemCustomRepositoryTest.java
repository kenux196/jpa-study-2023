package study.kenux.jpa.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import study.kenux.jpa.config.QuerydslConfig;
import study.kenux.jpa.domain.Item;
import study.kenux.jpa.domain.Store;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)
class ItemCustomRepositoryTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private JPAQueryFactory queryFactory;

    private ItemCustomRepository itemCustomRepository;

    @BeforeEach
    void init() {
        itemCustomRepository = new ItemCustomRepository(queryFactory);
    }

    @Test
    void test() {
        Store store = new Store("code", "apple store", "address", "123123");
        em.persist(store);

        Item item = new Item("item1", 1000, store);
        em.persist(item);

        em.flush();
        em.clear();

        final List<Item> result = itemCustomRepository.findAll();
        System.out.println("result = " + result);
    }

}
