package study.kenux.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.kenux.jpa.domain.PetOwner;

public interface PetOwnerRepository extends JpaRepository<PetOwner, Long> {
}
