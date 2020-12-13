package edu.ifma.engsoft.integracao.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
public class Imovel implements EntidadeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idImovel;

    private String tipoImovel;
    private String nome;
    private String endereco;
    private String bairro;
    private int cep;
    private double metragem;
    private int dormitorios;
    private int banheiros;
    private int suites;
    private int vagasGaragem;
    private BigDecimal valorAluguelSugerido;
    private String observacao;

    @Override
    public Long getId() {
        return idImovel;
    }
}
