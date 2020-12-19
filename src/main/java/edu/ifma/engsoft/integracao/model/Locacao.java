package edu.ifma.engsoft.integracao.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
public class Locacao implements EntidadeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idLocacao;

    private boolean ativo;
    private BigDecimal valorAluguel;
    private double percentualMulta;
    private int diaVencimento;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String observacao;

    @ManyToOne
    private Imovel imovel;

    @ManyToOne
    private Cliente inquilino;

    public Locacao() {
    }

    public Locacao(Imovel imovel, Cliente cliente) {
        this.imovel = imovel;
        this.inquilino = cliente;
    }

    @Override
    public Long getId() {
        return idLocacao;
    }

}
