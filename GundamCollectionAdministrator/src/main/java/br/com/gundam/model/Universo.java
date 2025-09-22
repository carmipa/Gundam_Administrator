package br.com.gundam.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "universo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Universo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String nome;

    @Column(nullable = false, unique = true, length = 8)
    private String sigla;

    @Column(length = 400)
    private String principaisSeries;

    @Column(length = 800)
    private String descricao;
}




