package study.kenux.jpa.test;

import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import study.kenux.jpa.domain.Member;
import study.kenux.jpa.domain.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public class MemberDataGenerator {

    private final EntityManager em;
    private final List<Member> memberList = Arrays.asList(
            new Member("MemberA", 10),
            new Member("MemberB", 20),
            new Member("MemberC", 30),
            new Member("MemberD", 40),
            new Member("MemberE", 50)
    );

    public void generate(List<Team> teamList) {
        final Optional<Team> teamA = teamList.stream()
                .filter(team -> team.getName().equals("TeamA"))
                .findAny();
        teamA.ifPresent(team -> memberList.forEach(member -> {
            member.changeTeam(team);
            em.persist(member);
        }));
        em.flush();
        em.clear();
    }

    public void generate() {
        memberList.forEach(em::persist);
        em.flush();
        em.clear();
    }

    public int getMemberCount() {
        return memberList.size();
    }
}
