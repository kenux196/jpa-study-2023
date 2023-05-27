package study.kenux.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.kenux.jpa.domain.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
