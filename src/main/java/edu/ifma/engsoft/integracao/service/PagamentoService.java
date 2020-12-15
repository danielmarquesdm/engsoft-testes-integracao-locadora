package edu.ifma.engsoft.integracao.service;

import edu.ifma.engsoft.integracao.model.Aluguel;
import edu.ifma.engsoft.integracao.util.exception.LocacaoException;

import java.math.BigDecimal;
import java.time.Period;

public class PagamentoService {
    public BigDecimal paga(BigDecimal valorAluguel, Aluguel aluguel) throws LocacaoException {
        if (valorAluguel.compareTo(aluguel.getValorPago()) < 0) {
            String erro = "ERRO AO PAGAR ALUGUEL. Valor de pagamento menor que o valor do aluguel.";
            throw new LocacaoException(erro);
        }

        if (aluguel.getDataDePagamento().isAfter(aluguel.getDataDeVencimento())) {
            int dias = Period.between(aluguel.getDataDePagamento(), aluguel.getDataDeVencimento()).getDays();
            BigDecimal multa = BigDecimal.valueOf(dias * 0.33);

            BigDecimal oitentaPorCento = aluguel.getValorPago().multiply(BigDecimal.valueOf(0.8));

            if (multa.compareTo(oitentaPorCento) <= 0) {
                return aluguel.getValorPago().add(multa);
            } else {
                return aluguel.getValorPago().add(oitentaPorCento);
            }
        } else {
            return aluguel.getValorPago();
        }
    }
}
