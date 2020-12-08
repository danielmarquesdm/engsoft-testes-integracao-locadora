package edu.ifma.engsoft.integracao.model;

import edu.ifma.engsoft.integracao.enums.TipoImovel;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
public class Imovel implements EntidadeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idImovel;

    @Enumerated(EnumType.STRING)
    private TipoImovel tipoImovel;
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

    @OneToMany(mappedBy = "imovel", cascade = CascadeType.ALL)
    private Set<Locacao> locacoesDoImovel = new LinkedHashSet<>();

    @Override
    public Long getId() {
        return idImovel;
    }
}
