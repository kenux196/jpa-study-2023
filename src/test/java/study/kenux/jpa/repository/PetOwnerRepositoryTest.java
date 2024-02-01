package study.kenux.jpa.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import study.kenux.jpa.domain.PetOwner;
import study.kenux.jpa.global.config.QuerydslConfig;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Import(QuerydslConfig.class)
class PetOwnerRepositoryTest {

    @Autowired
    PetOwnerRepository repository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("@DynamicUpdate 테스트")
    void dynamicUpdateTest() {
        PetOwner petOwner = new PetOwner("kenux", "010-1234-1234");
        repository.save(petOwner);
        em.flush();
        em.clear();
        petOwner.changeName("peter");
        repository.save(petOwner);
        em.flush();
        // @DynamicUpdate 사용하면 변경된 컬럼만 업데이트하게 됨
        // 그러나, 캐싱 히팅률이 낮아지므로 컬럼 수가 많은 테이블에 대해서만 사용하는 것이 좋다.
    }
}
