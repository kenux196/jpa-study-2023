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

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    private String address;

    private String contact;

    @Builder
    public Store(String code, String name, String address, String contact) {
        this.code = code;
        this.name = name;
        this.address = address;
        this.contact = contact;
    }
}