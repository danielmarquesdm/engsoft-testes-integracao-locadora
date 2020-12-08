package edu.ifma.engsoft.integracao.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
public class Aluguel {
    @Id
    private LocalDate dataDeVencimento;

    @ManyToOne
    private Locacao locacao;

    private BigDecimal valorPago;
    private LocalDate dataDePagamento;
    private String observacao;
}
