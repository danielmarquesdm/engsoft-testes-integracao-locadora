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
                .createQuery("from Aluguel a where a.dataDeVencimento = :dataDeVencimento", Aluguel.class)
                .setParameter("dataDeVencimento", dataDeVencimento)
                .getSingleResult();
    }

    public Aluguel salvaOuAtualiza(Aluguel aluguel){
        if (Objects.isNull(aluguel.getDataDeVencimento()))
            this.manager.persist(aluguel);
        else
            aluguel = this.manager.merge(aluguel);
        return aluguel;
    }

    public List<Aluguel> buscaTodos() {
        return manager
                .createQuery("select * from Aluguel", Aluguel.class)
                .getResultList();
    }

    public void remove(Aluguel aluguel){
        if(Objects.nonNull(aluguel.getDataDeVencimento())){
            manager.remove(aluguel);
            manager.flush();
        }
    }
}
