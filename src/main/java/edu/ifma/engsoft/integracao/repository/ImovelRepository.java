package edu.ifma.engsoft.integracao.repository;

import edu.ifma.engsoft.integracao.model.Imovel;

import javax.persistence.EntityManager;

public class ImovelRepository {
    private final EntityManager manager;
    private final DAOGenerico<Imovel> daoGenerico;

    public ImovelRepository(EntityManager manager){
        this.manager = manager;
        this.daoGenerico = new DAOGenerico<>(manager);
    }

    public Imovel buscaPor(Long id){
        return daoGenerico.buscaPorId(Imovel.class, id);
    }

    public Imovel salvaOuAtualiza(Imovel imovel){
        return daoGenerico.salvaOuAtualiza(imovel);
    }

    public void remove(Imovel imovel){
        daoGenerico.remove(imovel);
    }

}
