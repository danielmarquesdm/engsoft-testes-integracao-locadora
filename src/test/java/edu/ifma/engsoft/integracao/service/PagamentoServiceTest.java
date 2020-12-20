package edu.ifma.engsoft.integracao.service;

import edu.ifma.engsoft.integracao.builder.AluguelBuilder;
import edu.ifma.engsoft.integracao.model.Aluguel;
import edu.ifma.engsoft.integracao.util.exception.LocacaoException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PagamentoServiceTest {
    private PagamentoService pagamentoService;

    @Before
    public void setUp() {
        pagamentoService = new PagamentoService();
    }

    @Test(expected = LocacaoException.class)
    public void deveLancarExcecaoQuandoValorPagoForMenorQueAluguel() throws LocacaoException {
        Aluguel aluguel = AluguelBuilder.umAluguel()
                .comValorAluguel(BigDecimal.valueOf(1350))
                .comPagamentoNoValorDe(BigDecimal.valueOf(1000))
                .constroi();
        pagamentoService.paga(aluguel.getLocacao().getValorAluguel(), aluguel);
        assertThrows(LocacaoException.class,
                () -> pagamentoService.paga(aluguel.getLocacao().getValorAluguel(),
                        aluguel),
                "Não foi possível inserir pagamento. Valor minimo necessario.");
    }

    @Test
    public void deveRetornarValorAluguelSemMulta() throws LocacaoException {
        Aluguel aluguel = AluguelBuilder.umAluguel()
                .comValorAluguel(BigDecimal.valueOf(1350))
                .comPagamentoNoValorDe(BigDecimal.valueOf(1350))
                .comDataDeVencimento(LocalDate.of(2020,12,10))
                .comDataDePagamento(LocalDate.of(2020,12,10))
                .constroi();
        BigDecimal valorPagamento = pagamentoService.paga(aluguel.getLocacao().getValorAluguel(), aluguel);
        assertEquals(aluguel.getValorPago(), valorPagamento);
    }

    @Test
    public void deveRetornarValorAluguelComMulta() throws LocacaoException {
        Aluguel aluguel = AluguelBuilder.umAluguel()
                .comValorAluguel(BigDecimal.valueOf(1350))
                .comPagamentoNoValorDe(BigDecimal.valueOf(1350))
                .comDataDeVencimento(LocalDate.of(2020,12,10))
                .comDataDePagamento(LocalDate.of(2020,12,15))
                .constroi();

        BigDecimal multa = BigDecimal.valueOf(5 * 0.33);
        BigDecimal valorMaximoMulta = aluguel.getValorPago().multiply(BigDecimal.valueOf(0.8));
        double valorMulta = Math.round(Math.min(multa.doubleValue(), valorMaximoMulta.doubleValue()));
        BigDecimal pagamentoComMulta = aluguel.getLocacao().getValorAluguel().add(BigDecimal.valueOf(valorMulta));

        BigDecimal valorPagamento = pagamentoService.paga(aluguel.getLocacao().getValorAluguel(), aluguel);
        assertEquals(pagamentoComMulta, valorPagamento);
    }

}
