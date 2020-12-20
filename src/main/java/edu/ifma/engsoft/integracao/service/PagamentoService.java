package edu.ifma.engsoft.integracao.service;

import edu.ifma.engsoft.integracao.model.Aluguel;
import edu.ifma.engsoft.integracao.util.exception.LocacaoException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public class PagamentoService {
    public BigDecimal paga(BigDecimal valorAluguel, Aluguel aluguel) throws LocacaoException {
        BigDecimal valorPagamento;

        if (valorAluguel.compareTo(aluguel.getValorPago()) > 0) {
            String erro = "ERRO AO PAGAR ALUGUEL. Valor de pagamento menor que o valor do aluguel.";
            throw new LocacaoException(erro);
        } else {
            valorPagamento = calculaValorPagamento(aluguel.getDataDePagamento(), aluguel.getDataDeVencimento(), valorAluguel);
        }
        return valorPagamento;
    }

    private BigDecimal calculaValorPagamento(LocalDate dataDePagamento, LocalDate dataDeVencimento, BigDecimal valorAluguel) {
        BigDecimal valorPagamento;

        if (dataDePagamento.isAfter(dataDeVencimento)) {
            int dias = Math.abs(Period.between(dataDeVencimento, dataDePagamento).getDays());
            double valorMulta = Math.round(calculaValorMulta(dias, valorAluguel));
            valorPagamento = valorAluguel.add(BigDecimal.valueOf(valorMulta));
            return valorPagamento;
        }
        return valorAluguel;
    }

    private double calculaValorMulta(int dias, BigDecimal valorAluguel) {
        BigDecimal valorMulta = BigDecimal.valueOf(dias * 0.33);
        BigDecimal valorMaximoMulta = valorAluguel.multiply(BigDecimal.valueOf(0.8));

        return Math.min(valorMulta.doubleValue(), valorMaximoMulta.doubleValue());
    }
}
