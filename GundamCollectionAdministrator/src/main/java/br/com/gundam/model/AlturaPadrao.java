package br.com.gundam.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "altura_padrao")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AlturaPadrao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=30)
    private String descricao; // exemplo: "~ 13 cm"
}
