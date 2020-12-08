package edu.ifma.engsoft.integracao.service;

import edu.ifma.engsoft.integracao.model.Locacao;
import edu.ifma.engsoft.integracao.repository.LocacaoRepository;
import edu.ifma.engsoft.integracao.util.EMFactory;
import edu.ifma.engsoft.integracao.util.exception.LocacaoException;

import javax.persistence.EntityManager;
import java.util.Objects;

public class LocacaoService {
    private final LocacaoRepository repository;
    private final EntityManager manager;

    public LocacaoService() {
        this.manager = new EMFactory().getEntityManager();
        this.repository = new LocacaoRepository(manager);
    }

    public Locacao salvaOuAtualiza(Locacao locacao) throws LocacaoException {
        try {
            manager.getTransaction().begin();
            Locacao locacaoExistente = repository.buscaPor(locacao.getIdLocacao());

            if (Objects.nonNull(locacaoExistente) && locacao.equals(locacaoExistente)) {
                String erro = "Já existe locacao com o id informado.";
                throw new LocacaoException(erro);
            }

            Locacao locacaoSalva = repository.salvaOuAtualiza(locacao);
            manager.getTransaction().commit();

            return locacaoSalva;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Locacao buscaPor(Long id) throws LocacaoException {
        Locacao locacao = repository.buscaPor(id);

        if (Objects.nonNull(locacao)) {
            return locacao;
        }

        String erro = "Não foi encontrada nenhuma locaçãoo para o id informado.";
        throw new LocacaoException(erro);
    }

    public void remove(Locacao locacao) throws LocacaoException {
        Locacao locacaoExistente = repository.buscaPor(locacao.getIdLocacao());

        if(Objects.nonNull(locacaoExistente)){
            repository.remove(locacao);
        } else {
            String erro = "Não foi possível remover. Não existe locacao para o id informado.";
            throw new LocacaoException(erro);
        }
    }

}
