package br.com.gundam.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "escala")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Escala {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=20)
    private String rotulo;
}
