package edu.ifma.engsoft.integracao.repository;

import edu.ifma.engsoft.integracao.model.Aluguel;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class AluguelRepository {
    private final EntityManager manager;

    public AluguelRepository(EntityManager manager) {
        this.manager = manager;
    }

    public List<Aluguel> buscarPor(LocalDate dataDeVencimento) {
        return manager
                .createQuery("from Aluguel a where a.dataDeVencimento = :dataDeVencimento", Aluguel.class)
                .setParameter("dataDeVencimento", dataDeVencimento)
                .getResultList();
    }

    public List<Aluguel> buscarTodos() {
        return manager
                .createQuery("from Aluguel", Aluguel.class)
                .getResultList();
    }
}
