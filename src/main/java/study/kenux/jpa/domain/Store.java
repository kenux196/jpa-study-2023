package study.kenux.jpa.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "store")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    private String address;

    private String contact;

    public Store(String name, String address) {
        this.name = name;
        this.address = address;
    }

    @Builder
    public Store(String name, String address, String contact) {
        this.name = name;
        this.address = address;
        this.contact = contact;
    }
}
