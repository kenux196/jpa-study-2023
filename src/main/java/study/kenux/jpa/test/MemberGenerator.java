package study.kenux.jpa.test;

import jakarta.persistence.EntityManager;
import study.kenux.jpa.domain.Member;
import study.kenux.jpa.domain.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MemberGenerator extends DataGenerator {

    private final List<Member> memberList = Arrays.asList(
            new Member("MemberA", 10),
            new Member("MemberB", 20),
            new Member("MemberC", 30),
            new Member("MemberD", 40),
            new Member("MemberE", 50)
    );

    protected MemberGenerator(EntityManager em) {
        super(em);
    }

    @Override
    public void generate() {
        final List<Team> teamList = em.createQuery("select t from Team t", Team.class)
                .getResultList();

        final Optional<Team> teamA = teamList.stream()
                .filter(team -> team.getName().equals("TeamA"))
                .findAny();
        teamA.ifPresent(team -> memberList.forEach(member -> {
            member.changeTeam(team);
            em.persist(member);
        }));
    }
}
