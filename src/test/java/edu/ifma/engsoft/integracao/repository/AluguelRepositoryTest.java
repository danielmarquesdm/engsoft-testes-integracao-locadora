package edu.ifma.engsoft.integracao.repository;

import edu.ifma.engsoft.integracao.builder.AluguelBuilder;
import edu.ifma.engsoft.integracao.model.Aluguel;
import edu.ifma.engsoft.integracao.util.EMFactory;
import edu.ifma.engsoft.integracao.util.exception.LocacaoException;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AluguelRepositoryTest {
    private static EMFactory factory;
    private EntityManager manager;
    private AluguelRepository aluguelRepository;
    private Aluguel aluguel;

    @Before
    public void setUp() {
        factory = new EMFactory();
        manager = factory.getEntityManager();
        manager.getTransaction().begin();
        aluguelRepository = new AluguelRepository(manager);
        aluguel = AluguelBuilder.umAluguel().constroi();
    }

    @AfterEach
    public void after() {
        manager.getTransaction().rollback();
    }

    @AfterAll
    public static void end() {
        factory.close();
    }

    @Test
    public void deveAtualizarAluguel() {
        Aluguel aluguel = AluguelBuilder.umAluguel().constroi();
        aluguelRepository.salvaOuAtualiza(aluguel);
        aluguel.setObservacao("um otimo lugar");
        aluguelRepository.salvaOuAtualiza(aluguel);

        manager.flush();
        manager.clear();

        Aluguel aluguelEncontrado = aluguelRepository.buscaPor(aluguel.getDataDeVencimento());
        assertEquals("um otimo lugar", aluguelEncontrado.getObservacao());
    }

    @Test
    public void deveSalvarAluguel() {
        Aluguel aluguel = AluguelBuilder.umAluguel().constroi();
        aluguelRepository.salvaOuAtualiza(aluguel);

        manager.flush();
        manager.clear();

        Aluguel aluguelSalvo = aluguelRepository.buscaPor(aluguel.getDataDeVencimento());
        assertNotNull(aluguelSalvo);
    }

    @Test
    public void deveEncontrarAluguel() {
        aluguelRepository.salvaOuAtualiza(aluguel);
        Aluguel aluguelSalvo = aluguelRepository.buscaPor(aluguel.getDataDeVencimento());
        assertNotNull(aluguelSalvo);
        assertEquals(aluguel, aluguelSalvo);
    }

    @Test
    public void deveExcluirAluguel() {
        aluguelRepository.remove(aluguel);
        assertThrows(NoResultException.class,
                () -> aluguelRepository.buscaPor(aluguel.getDataDeVencimento()),
                "Deveria ter lançado a exceção NoResultException");
    }

    @Test
    public void deveRetornarTodosAlugueisPagos() {
        Aluguel aluguel1 = AluguelBuilder.umAluguel()
                .paraUmCliente("Daniel")
                .comPagamentoNoValorDe(BigDecimal.valueOf(1800))
                .constroi();
        Aluguel aluguel2 = AluguelBuilder.umAluguel()
                .constroi();
        Aluguel aluguel3 = AluguelBuilder.umAluguel()
                .paraUmCliente("Daniel")
                .comPagamentoNoValorDe(BigDecimal.valueOf(1800))
                .constroi();
        Aluguel aluguel4 = AluguelBuilder.umAluguel()
                .constroi();

        List<Aluguel> alugueisEsperados = new ArrayList<>();
        alugueisEsperados.add(aluguel1);
        alugueisEsperados.add(aluguel3);

        aluguelRepository.salvaOuAtualiza(aluguel1);
        aluguelRepository.salvaOuAtualiza(aluguel2);
        aluguelRepository.salvaOuAtualiza(aluguel3);
        aluguelRepository.salvaOuAtualiza(aluguel4);

        manager.flush();
        manager.clear();

        List<Aluguel> alugueisAtuais = aluguelRepository.buscaAlugueisPagosPor("Daniel");
        assertEquals(2, alugueisAtuais.size());
        Matchers.arrayContainingInAnyOrder(alugueisEsperados).matches(alugueisAtuais);
    }

    @Test
    public void deveRetornarAlugueisPagosComAtrasoEm() {
        LocalDate dataVencimento = LocalDate.of(2020, 10, 10);
        Aluguel aluguel1 = AluguelBuilder.umAluguel()
                .comDataDeVencimento(dataVencimento)
                .emAtraso()
                .constroi();
        Aluguel aluguel2 = AluguelBuilder.umAluguel()
                .comDataDeVencimento(dataVencimento)
                .comDataDePagamento(LocalDate.of(2020, 10, 5))
                .constroi();
        Aluguel aluguel3 = AluguelBuilder.umAluguel()
                .comDataDeVencimento(LocalDate.of(2020, 10, 8))
                .comDataDePagamento(LocalDate.of(2020, 10, 5))
                .constroi();
        Aluguel aluguel4 = AluguelBuilder.umAluguel()
                .comDataDeVencimento(dataVencimento)
                .emAtraso()
                .constroi();

        List<Aluguel> alugueisEsperados = new ArrayList<>();
        alugueisEsperados.add(aluguel1);
        alugueisEsperados.add(aluguel4);

        aluguelRepository.salvaOuAtualiza(aluguel1);
        aluguelRepository.salvaOuAtualiza(aluguel2);
        aluguelRepository.salvaOuAtualiza(aluguel3);
        aluguelRepository.salvaOuAtualiza(aluguel4);

        manager.flush();
        manager.clear();

        List<Aluguel> alugueisAtuais = aluguelRepository.buscaAlugueisEmAtraso(dataVencimento);
        assertEquals(2, alugueisAtuais.size());
        Matchers.arrayContainingInAnyOrder(alugueisEsperados).matches(alugueisAtuais);
    }

    @Test
    public void deveInserirPagamento() throws LocacaoException {
        BigDecimal pagamento = BigDecimal.valueOf(1350);
        Aluguel aluguelEsperado = AluguelBuilder.umAluguel().comPagamentoNoValorDe(pagamento).constroi();
        aluguelRepository.insertPagamento(pagamento, aluguelEsperado);
        Aluguel aluguelAtual = aluguelRepository.buscaPorId(aluguelEsperado.getIdAluguel());
        assertEquals(aluguelEsperado.getValorPago(), aluguelAtual.getValorPago());
    }

    @Test(expected = LocacaoException.class)
    public void deveLancarExcecaoAoInserirPagamento() throws LocacaoException {
        BigDecimal pagamento = BigDecimal.valueOf(1350);
        Aluguel aluguelEsperado = AluguelBuilder.umAluguel()
                .comPagamentoNoValorDe(BigDecimal.valueOf(1500)).constroi();
        aluguelRepository.insertPagamento(pagamento, aluguelEsperado);
        assertThrows(LocacaoException.class,
                () -> aluguelRepository.buscaPorId(aluguelEsperado.getIdAluguel()),
                "Não foi possível inserir pagamento. Valor minimo necessario.");
    }

    @Test
    public void deveRetornarValorAluguelSemMulta() {
        LocalDate vencimento = LocalDate.of(2020, 10, 10);
        Aluguel aluguelEsperado = AluguelBuilder.umAluguel()
                .comDataDeVencimento(vencimento)
                .comDataDePagamento(vencimento)
                .constroi();
        BigDecimal valorAtual = aluguelRepository.calculaValorAluguel(vencimento, aluguelEsperado.getDataDePagamento(),
                aluguelEsperado.getValorPago());
        assertEquals(aluguelEsperado.getValorPago(), valorAtual);
    }

    @Test
    public void deveRetornarValorAluguelComMulta() {
        LocalDate vencimento = LocalDate.of(2020, 10, 10);
        Aluguel aluguelEsperado = AluguelBuilder.umAluguel()
                .comDataDeVencimento(vencimento)
                .comDataDePagamento(vencimento.plusDays(5))
                .constroi();
        BigDecimal valorEsperado = aluguelEsperado.getValorPago().add(BigDecimal.valueOf(5 * 0.33));
        BigDecimal valorAtual = aluguelRepository
                .calculaValorAluguel(vencimento, aluguelEsperado.getDataDePagamento(),
                        aluguelEsperado.getValorPago());
        assertEquals(valorEsperado, valorAtual);
    }

}
