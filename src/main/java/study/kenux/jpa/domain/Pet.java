package study.kenux.jpa.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "pet")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "pet_type", nullable = false)
    private PetType petType;

    private Integer age;

    private OffsetDateTime visitTime;

    private OffsetDateTime reservedTime;

    public Pet(String name, PetType petType) {
        this.name = name;
        this.petType = petType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_owner_id")
    private PetOwner petOwner;

    public void updateOwner(PetOwner owner) {
        this.petOwner = owner;
    }

    public void reserveVisit(OffsetDateTime reservedTime) {
        this.reservedTime = reservedTime;
    }
}
