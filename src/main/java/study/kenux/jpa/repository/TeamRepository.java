package study.kenux.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import study.kenux.jpa.domain.Team;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query(value = "select t from Team t join t.members")
    List<Team> findAllWithJPQL();


    @Query(value = "select t from Team t join fetch t.members")
    List<Team> findAllWithJPQL_fetchJoin();

    @Query(value = "select t from Team t join t.members m ")
    List<Team> findAll3();
}
