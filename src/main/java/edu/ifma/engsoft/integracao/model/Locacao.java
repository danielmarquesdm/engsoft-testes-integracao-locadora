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

    private BigDecimal valorAluguel;
    private double percentualMulta;
    private int diaVencimento;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private boolean ativo;
    private String observacao;

    @ManyToOne(cascade = CascadeType.ALL)
    private Imovel imovel;

    @ManyToOne(cascade = CascadeType.ALL)
    private Cliente inquilino;

    public Locacao(Imovel imovel, Cliente cliente) {
        this.imovel = imovel;
        this.inquilino = cliente;
    }

    @Override
    public Long getId() {
        return idLocacao;
    }

}
