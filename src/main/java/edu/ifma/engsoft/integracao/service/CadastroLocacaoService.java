package edu.ifma.engsoft.integracao.service;

import edu.ifma.engsoft.integracao.model.Locacao;
import edu.ifma.engsoft.integracao.repository.LocacaoRepository;
import edu.ifma.engsoft.integracao.util.EMFactory;
import edu.ifma.engsoft.integracao.util.exception.CadastroException;

import javax.persistence.EntityManager;

public class CadastroLocacaoService {
    private final LocacaoRepository repository;
    private final EntityManager manager;

    public CadastroLocacaoService() {
        this.manager = new EMFactory().getEntityManager();
        this.repository = new LocacaoRepository(manager);
    }

    public Locacao salva(Locacao locacao) throws CadastroException {
        try {
            manager.getTransaction().begin();
            Locacao locacaoExistente = repository.buscaPor(locacao.getIdLocacao());

            if (locacaoExistente != null && locacao.equals(locacaoExistente)) {
                String erro = "Já existe locacao com o id informado.";
                throw new CadastroException(erro);
            }

            Locacao locacaoSalva = repository.salvaOuAtualiza(locacao);
            manager.getTransaction().commit();

            return locacaoSalva;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
