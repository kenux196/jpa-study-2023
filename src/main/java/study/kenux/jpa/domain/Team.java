package study.kenux.jpa.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "team")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Team(String name) {
        this.name = name;
    }

    public void updateTeamName(String name) {
        this.name = name;
    }
}
