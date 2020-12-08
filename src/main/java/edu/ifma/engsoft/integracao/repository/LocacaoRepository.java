package edu.ifma.engsoft.integracao.repository;

import edu.ifma.engsoft.integracao.model.Locacao;

import javax.persistence.EntityManager;

public class LocacaoRepository {

    private final EntityManager manager;
    private final DAOGenerico<Locacao> daoGenerico;


    public LocacaoRepository(EntityManager manager) {
        this.manager = manager;
        this.daoGenerico = new DAOGenerico<>(manager);
    }

    public Locacao buscaPor(Long id) {
        return daoGenerico.buscarPorId(Locacao.class, id);
    }

    public Locacao salvaOuAtualiza(Locacao locacao) {
        return daoGenerico.salvaOuAtualiza(locacao);
    }

    public void remove(Locacao locacao) {
        daoGenerico.remove(locacao);
    }

}
