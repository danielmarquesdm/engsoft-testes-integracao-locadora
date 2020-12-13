package edu.ifma.engsoft.integracao.repository;

import edu.ifma.engsoft.integracao.model.Aluguel;
import edu.ifma.engsoft.integracao.util.exception.LocacaoException;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class AluguelRepository {
    private final EntityManager manager;
    private final DAOGenerico<Aluguel> daoGenerico;

    public AluguelRepository(EntityManager manager) {
        this.manager = manager;
        this.daoGenerico = new DAOGenerico<>(manager);
    }

    public Aluguel buscaPor(LocalDate dataDeVencimento) {
        return manager
                .createQuery("SELECT a FROM Aluguel a where a.dataDeVencimento = :dataDeVencimento", Aluguel.class)
                .setParameter("dataDeVencimento", dataDeVencimento)
                .getSingleResult();
    }

    public Aluguel buscaPorId(Long id) {
        return this.manager.find(Aluguel.class, id);
    }

    public Aluguel salvaOuAtualiza(Aluguel aluguel) {
        return daoGenerico.salvaOuAtualiza(aluguel);
    }

    public void remove(Aluguel aluguel) {
        daoGenerico.remove(aluguel);
    }

    public List<Aluguel> buscaAlugueisPagosPor(String nomeCliente) {
        return this.manager.createQuery("SELECT a FROM Aluguel a INNER JOIN Locacao l ON a.locacao.id = l.id INNER JOIN Cliente c ON c.id = l.inquilino.id WHERE c.nomeCliente = :nome AND a.dataDePagamento <= dataDeVencimento", Aluguel.class)
                .setParameter("nome", nomeCliente)
                .getResultList();
    }

    public List<Aluguel> buscaAlugueisEmAtraso(LocalDate dataVencimento) {
        return this.manager.createQuery("SELECT a FROM Aluguel a WHERE a.dataDeVencimento = :dataVencimento AND a.dataDePagamento > a.dataDeVencimento", Aluguel.class)
                .setParameter("dataVencimento", dataVencimento)
                .getResultList();
    }

    public void insertPagamento(BigDecimal pagamento, Aluguel aluguel) throws LocacaoException {
        if (pagamento.compareTo(aluguel.getValorPago()) < 0) {
            String erro = "Não foi possível inserir pagamento. Valor minimo necessario.";
            throw new LocacaoException(erro);
        }

        aluguel.setValorPago(pagamento);
        salvaOuAtualiza(aluguel);
    }

    public BigDecimal calculaValorAluguel(LocalDate vencimento, LocalDate pagamento, BigDecimal valor) {
        if (pagamento.isAfter(vencimento)) {
            int dias = Period.between(vencimento, pagamento).getDays();
            BigDecimal multa = BigDecimal.valueOf(dias * 0.33);

            BigDecimal oitentaPorCento = valor.multiply(BigDecimal.valueOf(0.8));

            if (multa.compareTo(oitentaPorCento) <= 0) {
                return valor.add(multa);
            } else {
                return valor.add(oitentaPorCento);
            }
        } else {
            return valor;
        }
    }
}
