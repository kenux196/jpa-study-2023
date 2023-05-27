package study.kenux.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.kenux.jpa.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
