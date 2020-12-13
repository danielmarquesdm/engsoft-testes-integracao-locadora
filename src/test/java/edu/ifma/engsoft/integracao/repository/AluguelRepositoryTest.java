package edu.ifma.engsoft.integracao.repository;

import edu.ifma.engsoft.integracao.builder.AluguelBuilder;
import edu.ifma.engsoft.integracao.builder.LocacaoBuilder;
import edu.ifma.engsoft.integracao.model.Aluguel;
import edu.ifma.engsoft.integracao.util.EMFactory;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
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
        aluguelRepository.salva(aluguel);
        aluguel.setObservacao("um otimo lugar");
        aluguelRepository.atualiza(aluguel);

        manager.flush();
        manager.clear();

        Aluguel aluguelEncontrado = aluguelRepository.buscaPor(aluguel.getDataDeVencimento());
        assertEquals("um otimo lugar", aluguelEncontrado.getObservacao());
    }

    @Test
    public void deveSalvarAluguel() {
        Aluguel aluguel = AluguelBuilder.umAluguel().constroi();
        aluguelRepository.salva(aluguel);

        manager.flush();
        manager.clear();

        Aluguel aluguelSalvo = aluguelRepository.buscaPor(aluguel.getDataDeVencimento());
        assertNotNull(aluguelSalvo);
    }

    @Test
    public void deveEncontrarAluguel() {
        aluguelRepository.salva(aluguel);
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
                .comVencimento(LocalDate.of(2020,12,10))
                .comDataDePagamento(LocalDate.of(2020,12,9))
                .comPagamentoNoValorDe(BigDecimal.valueOf(1800))
                .constroi();
        Aluguel aluguel2 = AluguelBuilder.umAluguel()
                .comVencimento(LocalDate.of(2020,12,5))
                .constroi();
        Aluguel aluguel3 = AluguelBuilder.umAluguel()
                .comVencimento(LocalDate.of(2020,12,15))
                .comDataDePagamento(LocalDate.of(2020,12,9))
                .comPagamentoNoValorDe(BigDecimal.valueOf(1800))
                .constroi();
        Aluguel aluguel4 = AluguelBuilder.umAluguel()
                .comVencimento(LocalDate.of(2020,12,10))
                .constroi();

        List<Aluguel> alugueisEsperados = new ArrayList<>();
        alugueisEsperados.add(aluguel1);
        alugueisEsperados.add(aluguel3);

        List<Aluguel> alugueisAtuais = aluguelRepository.buscaAlugueisPagosPor("Akila");
        assertEquals(2, alugueisAtuais.size());
        Matchers.arrayContainingInAnyOrder(alugueisEsperados).matches(alugueisAtuais);
    }

}
