package study.kenux.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.kenux.jpa.domain.Store;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByName(String name);
}
