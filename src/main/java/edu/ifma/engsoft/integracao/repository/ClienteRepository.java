package edu.ifma.engsoft.integracao.repository;

import edu.ifma.engsoft.integracao.model.Cliente;

import javax.persistence.EntityManager;

public class ClienteRepository {

    private final EntityManager manager;
    private final DAOGenerico<Cliente> daoGenerico;

    public ClienteRepository(EntityManager manager) {
        this.manager = manager;
        this.daoGenerico = new DAOGenerico<>(manager);
    }

    public Cliente buscaPor(Long id) {
        return daoGenerico.buscaPorId(Cliente.class, id);
    }

    public Cliente salvaOuAtualiza(Cliente cliente) {
        return daoGenerico.salvaOuAtualiza(cliente);
    }

    public void remove(Cliente cliente) {
        daoGenerico.remove(cliente);
    }

}
