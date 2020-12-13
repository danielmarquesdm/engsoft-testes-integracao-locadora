package edu.ifma.engsoft.integracao.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Cliente implements EntidadeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idCliente;

    private String nomeCliente;
    private String cpf;
    private int celular;
    private String email;
    private LocalDate dtNascimento;

    @Override
    public Long getId() {
        return idCliente;
    }
}
