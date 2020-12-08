package edu.ifma.engsoft.integracao.service;

import edu.ifma.engsoft.integracao.model.Cliente;
import edu.ifma.engsoft.integracao.repository.ClienteRepository;
import edu.ifma.engsoft.integracao.util.EMFactory;
import edu.ifma.engsoft.integracao.util.exception.LocacaoException;

import javax.persistence.EntityManager;
import java.util.Objects;

public class ClienteService {
    private final ClienteRepository repository;
    private final EntityManager manager;

    public ClienteService(){
        this.manager = new EMFactory().getEntityManager();
        this.repository = new ClienteRepository(manager);
    }

    public Cliente salvaOuAtualiza(Cliente cliente) throws LocacaoException {
        try {
            manager.getTransaction().begin();
            Cliente clienteExistente = repository.buscaPor(cliente.getIdCliente());

            if (Objects.nonNull(clienteExistente) && cliente.equals(clienteExistente)) {
                String erro = "Já existe cliente com o id informado.";
                throw new LocacaoException(erro);
            }

            Cliente clienteSalvo = repository.salvaOuAtualiza(cliente);
            manager.getTransaction().commit();

            return clienteSalvo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Cliente buscaPor(Long id) throws LocacaoException {
        Cliente cliente = repository.buscaPor(id);

        if (Objects.nonNull(cliente)) {
            return cliente;
        }

        String erro = "Não foi encontrada nenhuma locaçãoo para o id informado.";
        throw new LocacaoException(erro);
    }

    public void remove(Cliente cliente) throws LocacaoException {
        Cliente clienteExistente = repository.buscaPor(cliente.getIdCliente());

        if(Objects.nonNull(clienteExistente)){
            repository.remove(cliente);
        } else {
            String erro = "Não foi possível remover. Não existe cliente para o id informado.";
            throw new LocacaoException(erro);
        }
    }

}
