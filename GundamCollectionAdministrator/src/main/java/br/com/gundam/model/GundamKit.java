package br.com.gundam.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "gundam_kits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GundamKit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Modelo com até 200 chars
    @NotBlank
    @Size(max = 200)
    @Column(length = 200, nullable = false)
    private String modelo;

    // Fabricante default "Bandai"
    @NotBlank
    @Size(max = 100)
    @Builder.Default
    @Column(length = 100, nullable = false)
    private String fabricante = "Bandai";

    // Preço pago
    @DecimalMin("0.00")
    @Digits(integer = 10, fraction = 2)
    @Column(precision = 12, scale = 2)
    private BigDecimal preco;

    // Data da compra
    private LocalDate dataCompra;

    // URLs diversas (opcionais)
    @Size(max = 500)
    @Column(length = 500)
    private String capaUrl;

    @Size(max = 500)
    @Column(length = 500)
    private String fotoCaixaUrl;

    @Size(max = 500)
    @Column(length = 500)
    private String fotoMontagemUrl;

    @Size(max = 500)
    @Column(length = 500)
    private String videoMontagemUrl;

    // horas de montagem (opcional)
    @Min(0)
    private Integer horasMontagem;

    // Relacionamentos (catálogos fixos)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "grade_id")
    private Grade grade;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "escala_id")
    private Escala escala;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "altura_padrao_id")
    private AlturaPadrao alturaPadrao;

    // Audit
    @Builder.Default
    private Instant createdAt = Instant.now();

    // Universo/linha do tempo
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "universo_id")
    private Universo universo;

    @Size(max = 2000)
    @Column(length = 2000)
    private String observacao;
}
