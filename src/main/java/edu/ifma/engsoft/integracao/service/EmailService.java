package edu.ifma.engsoft.integracao.service;

import edu.ifma.engsoft.integracao.model.Cliente;
import edu.ifma.engsoft.integracao.model.Locacao;
import edu.ifma.engsoft.integracao.repository.LocacaoRepository;
import edu.ifma.engsoft.integracao.util.exception.LocacaoException;

import javax.persistence.EntityManager;
import java.util.List;


public class EmailService {
    private EntityManager manager;
    private LocacaoRepository repository = new LocacaoRepository(manager);

    public boolean enviaEmailParaClientesEmAtraso() {
        List<Locacao> locacoes = repository.emAtraso();
        locacoes.forEach(l -> {
            try {
                notificaCliente(l.getInquilino());
            } catch (LocacaoException e) {
                System.out.println("ERRO AO NOTIFICAR CLIENTE. " + e.getMessage());
            }
        });
        return true;
    }

    public void notificaCliente(Cliente cliente) throws LocacaoException {
        if (cliente != null) {
            System.out.println("Ola, " + cliente.getNomeCliente() + " Verificamos que seu aluguel nao foi pago na data prevista. Aproveite e pague!");
        } else {
            String erro = "CLIENTE NAO ENCONTRADO.";
            throw new LocacaoException(erro);
        }
    }
}
