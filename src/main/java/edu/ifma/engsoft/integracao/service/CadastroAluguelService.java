package edu.ifma.engsoft.integracao.service;

import edu.ifma.engsoft.integracao.model.Aluguel;
import edu.ifma.engsoft.integracao.repository.AluguelRepository;
import edu.ifma.engsoft.integracao.util.EMFactory;
import edu.ifma.engsoft.integracao.util.exception.LocacaoException;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class CadastroAluguelService {
    private final EntityManager manager;
    private final AluguelRepository repository;

    public CadastroAluguelService(){
        this.manager = new EMFactory().getEntityManager();
        this.repository = new AluguelRepository(manager);
    }

    public Aluguel salvaOuAtualiza(Aluguel aluguel) throws LocacaoException {
        try {
            manager.getTransaction().begin();
            Aluguel aluguelExistente = repository.buscaPor(aluguel.getDataDeVencimento());

            if (Objects.nonNull(aluguel) && aluguel.equals(aluguelExistente)) {
                String erro = "Já existe aluguel para a data de vencimento informada..";
                throw new LocacaoException(erro);
            }

            Aluguel aluguelSalvo = repository.salvaOuAtualiza(aluguel);
            manager.getTransaction().commit();

            return aluguelSalvo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Aluguel buscaPor(LocalDate dataDeVencimento) throws LocacaoException {
        Aluguel aluguel = repository.buscaPor(dataDeVencimento);

        if (Objects.nonNull(aluguel)) {
            return aluguel;
        }

        String erro = "Não existe aluguel para a data de vencimento informada.";
        throw new LocacaoException(erro);
    }

    public List<Aluguel> buscaTodos(){
        return repository.buscaTodos();
    }

    public void remove(Aluguel aluguel) throws LocacaoException {
        Aluguel aluguelExistente = repository.buscaPor(aluguel.getDataDeVencimento());

        if(Objects.nonNull(aluguelExistente)){
            repository.remove(aluguel);
        } else {
            String erro = "Não foi possível remover. Não existe aluguel para a data de vencimento informada.";
            throw new LocacaoException(erro);
        }
    }

}
