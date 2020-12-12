package edu.ifma.engsoft.integracao.service;

import edu.ifma.engsoft.integracao.model.Cliente;
import edu.ifma.engsoft.integracao.repository.ClienteRepository;
import edu.ifma.engsoft.integracao.util.exception.LocacaoException;

import java.util.Objects;

public class ClienteService {
    private ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente salvaOuAtualiza(Cliente cliente) throws LocacaoException {
        Cliente clienteDoBanco = clienteRepository.buscaPor(cliente.getIdCliente());
        if (Objects.isNull(clienteDoBanco)) {
            return clienteRepository.salvaOuAtualiza(cliente);
        } else {
            String erro = "Usuário já existe.";
            throw new LocacaoException(erro);
        }
    }
}
