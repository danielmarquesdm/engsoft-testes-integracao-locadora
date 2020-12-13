package edu.ifma.engsoft.integracao.builder;

import edu.ifma.engsoft.integracao.model.Aluguel;
import edu.ifma.engsoft.integracao.model.Locacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AluguelBuilder {
    private Aluguel aluguel;

    private AluguelBuilder() {
    }

    public static AluguelBuilder umAluguel() {
        Locacao locacao = LocacaoBuilder.umaLocacao().constroi();
        AluguelBuilder builder = new AluguelBuilder();
        builder.aluguel = new Aluguel();
        builder.aluguel.setDataDeVencimento(LocalDate.of(2020, 12, 15));
        builder.aluguel.setDataDePagamento(LocalDate.of(2020, 12, 4));
        builder.aluguel.setLocacao(locacao);
        builder.aluguel.setObservacao("Proximo à praia");
        builder.aluguel.setValorPago(BigDecimal.valueOf(1350));
        return builder;
    }

    public AluguelBuilder comVencimento(LocalDate vencimento) {
        aluguel.setDataDeVencimento(vencimento);
        return this;
    }

    public AluguelBuilder comDataDePagamento(LocalDate dataPagamento){
        aluguel.setDataDePagamento(dataPagamento);
        return this;
    }

    public AluguelBuilder comPagamentoNoValorDe(BigDecimal valor){
        aluguel.setValorPago(valor);
        return this;
    }

    public Aluguel constroi() {
        return aluguel;
    }
}
