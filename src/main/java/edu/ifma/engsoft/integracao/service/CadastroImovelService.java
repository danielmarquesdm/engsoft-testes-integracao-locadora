package edu.ifma.engsoft.integracao.service;

import edu.ifma.engsoft.integracao.model.Imovel;
import edu.ifma.engsoft.integracao.repository.ImovelRepository;
import edu.ifma.engsoft.integracao.util.EMFactory;
import edu.ifma.engsoft.integracao.util.exception.LocacaoException;

import javax.persistence.EntityManager;
import java.util.Objects;

public class CadastroImovelService {
    private final ImovelRepository repository;
    private final EntityManager manager;

    public CadastroImovelService(){
        this.manager = new EMFactory().getEntityManager();
        this.repository = new ImovelRepository(manager);
    }

    public Imovel salvaOuAtualiza(Imovel imovel) throws LocacaoException {
        try {
            manager.getTransaction().begin();
            Imovel imovelExistente = repository.buscaPor(imovel.getIdImovel());

            if (Objects.nonNull(imovelExistente) && imovel.equals(imovelExistente)) {
                String erro = "Já existe Imovel com o id informado.";
                throw new LocacaoException(erro);
            }

            Imovel imovelSalvo = repository.salvaOuAtualiza(imovel);
            manager.getTransaction().commit();

            return imovelSalvo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Imovel buscaPor(Long id) throws LocacaoException {
        Imovel imovel = repository.buscaPor(id);

        if (Objects.nonNull(imovel)) {
            return imovel;
        }

        String erro = "Não foi encontrada nenhum imovel para o id informado.";
        throw new LocacaoException(erro);
    }

    public void remove(Imovel imovel) throws LocacaoException {
        Imovel imovelExistente = repository.buscaPor(imovel.getIdImovel());

        if(Objects.nonNull(imovelExistente)){
            repository.remove(imovel);
        } else {
            String erro = "Não foi possível remover. Não existe imovel para o id informado.";
            throw new LocacaoException(erro);
        }
    }

}
