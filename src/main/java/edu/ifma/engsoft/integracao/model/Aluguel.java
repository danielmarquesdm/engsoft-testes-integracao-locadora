package edu.ifma.engsoft.integracao.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
public class Aluguel implements EntidadeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idAluguel;

    @ManyToOne
    private Locacao locacao;

    private LocalDate dataDeVencimento;
    private BigDecimal valorPago;
    private LocalDate dataDePagamento;
    private String observacao;

    public Aluguel() {
    }

    @Override
    public Long getId() {
        return idAluguel;
    }
}
