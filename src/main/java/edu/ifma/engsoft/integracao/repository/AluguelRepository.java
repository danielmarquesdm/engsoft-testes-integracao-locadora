package edu.ifma.engsoft.integracao.repository;

import edu.ifma.engsoft.integracao.model.Aluguel;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class AluguelRepository {
    private final EntityManager manager;

    public AluguelRepository(EntityManager manager) {
        this.manager = manager;
    }

    public Aluguel buscaPor(LocalDate dataDeVencimento) {
        return manager
                .createQuery("SELECT a FROM Aluguel a where a.dataDeVencimento = :dataDeVencimento", Aluguel.class)
                .setParameter("dataDeVencimento", dataDeVencimento)
                .getSingleResult();
    }

    public void salva(Aluguel aluguel) {
        this.manager.persist(aluguel);
    }

    public void atualiza(Aluguel aluguel) {
        this.manager.merge(aluguel);
    }

    public void remove(Aluguel aluguel) {
        if (Objects.nonNull(aluguel.getDataDeVencimento())) {
            manager.remove(aluguel);
            manager.flush();
        }
    }

    public List<Aluguel> buscaAlugueisPagosPor(String nomeCliente) {
        return this.manager.createQuery("SELECT a FROM Aluguel a INNER JOIN Locacao l ON a.locacao.id = l.id INNER JOIN Cliente c ON C.id = l.cliente.id WHERE c.nomeCliente = :nome", Aluguel.class)
        .setParameter("nome", nomeCliente)
        .getResultList();
    }
}
