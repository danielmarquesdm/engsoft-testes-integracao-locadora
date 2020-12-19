package edu.ifma.engsoft.integracao.repository;

import edu.ifma.engsoft.integracao.builder.LocacaoBuilder;
import edu.ifma.engsoft.integracao.model.Imovel;
import edu.ifma.engsoft.integracao.model.Locacao;
import edu.ifma.engsoft.integracao.util.EMFactory;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

public class LocacaoRepositoryTest {
    private EntityManager manager;
    private static EMFactory factory;
    private LocacaoRepository locacaoRepository;

    @Before
    public void setUp() {
        factory = new EMFactory();
        manager = factory.getEntityManager();
        locacaoRepository = new LocacaoRepository(manager);
    }

    @BeforeEach
    public void inicio() {
        manager.getTransaction().begin();
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
    public void deveAtualizarLocacao() {
        Locacao locacaoEsperada = LocacaoBuilder.umaLocacao().constroi();
        locacaoRepository.salvaOuAtualiza(locacaoEsperada);
        locacaoEsperada.setAtivo(false);
        Locacao locacaoSalva = locacaoRepository.salvaOuAtualiza(locacaoEsperada);
        manager.clear();
        assertFalse(locacaoSalva.isAtivo());
    }

    @Test
    public void deveSalvarLocacao() {
        Locacao locacaoEsperada = LocacaoBuilder
                .umaLocacao()
                .constroi();
        Locacao locacaoSalva = locacaoRepository.salvaOuAtualiza(locacaoEsperada);
        assertNotNull(locacaoSalva);
        assertEquals(locacaoEsperada, locacaoSalva);
    }

    @Test
    public void deveEncontrarLocacao() {
        Locacao locacaoEsperada = LocacaoBuilder
                .umaLocacao()
                .constroi();
        Locacao locacaoSalva = locacaoRepository.salvaOuAtualiza(locacaoEsperada);
        manager.clear();
        List<Locacao> locacaoEncontrada = locacaoRepository.buscaPor(locacaoSalva.getDataInicio());
        assertNotNull(locacaoEncontrada);
    }

    @Test
    public void deveExcluirLocacao() {
        Locacao locacao = LocacaoBuilder.umaLocacao().constroi();
        locacaoRepository.salvaOuAtualiza(locacao);
        locacaoRepository.remove(locacao);
        List<Locacao> locacaoEncontrada = locacaoRepository.buscaPor(locacao.getDataInicio());
        assertEquals(Collections.emptyList(), locacaoEncontrada);
    }

    @Test
    public void deveRecuperarImoveisDisponiveisParaAlugar() {
        Locacao locacao1 = LocacaoBuilder.umaLocacao()
                .noBairro("Araçagy").tipo("casa").ativo(false).constroi();
        Locacao locacao2 = LocacaoBuilder.umaLocacao()
                .noBairro("Cohama").tipo("apartamento").ativo(false).constroi();
        Locacao locacao3 = LocacaoBuilder.umaLocacao()
                .noBairro("Cohatrac").tipo("casa").ativo(false).constroi();
        Locacao locacao4 = LocacaoBuilder.umaLocacao()
                .noBairro("Araçagy").tipo("apartamento").ativo(false).constroi();
        Locacao locacao5 = LocacaoBuilder.umaLocacao()
                .noBairro("Araçagy").tipo("apartamento").ativo(true).constroi();
        Locacao locacao6 = LocacaoBuilder.umaLocacao()
                .noBairro("Araçagy").tipo("apartamento").ativo(false).constroi();

        List<Imovel> imoveisEsperados = new ArrayList<>();
        imoveisEsperados.add(locacao4.getImovel());
        imoveisEsperados.add(locacao6.getImovel());

        locacaoRepository.salvaOuAtualiza(locacao1);
        locacaoRepository.salvaOuAtualiza(locacao2);
        locacaoRepository.salvaOuAtualiza(locacao3);
        locacaoRepository.salvaOuAtualiza(locacao4);
        locacaoRepository.salvaOuAtualiza(locacao5);
        locacaoRepository.salvaOuAtualiza(locacao6);

        manager.clear();

        List<Imovel> imoveisAtuais = locacaoRepository.buscaImoveisPor("Araçagy");
        assertNotNull(imoveisAtuais);
        Matchers.arrayContainingInAnyOrder(imoveisEsperados).matches(imoveisAtuais);
    }

    @Test
    public void deveRecuperarImoveisDisponiveisPor() {
        Locacao locacao1 = LocacaoBuilder.umaLocacao().ativo(false).comValorAluguelSugerido(BigDecimal.valueOf(1499.9999)).constroi();
        Locacao locacao2 = LocacaoBuilder.umaLocacao().ativo(true).comValorAluguelSugerido(BigDecimal.valueOf(1500)).constroi();
        Locacao locacao3 = LocacaoBuilder.umaLocacao().ativo(false).comValorAluguelSugerido(BigDecimal.valueOf(2500)).constroi();
        Locacao locacao4 = LocacaoBuilder.umaLocacao().ativo(false).comValorAluguelSugerido(BigDecimal.valueOf(1500.0001)).constroi();
        Locacao locacao5 = LocacaoBuilder.umaLocacao().ativo(true).comValorAluguelSugerido(BigDecimal.valueOf(1175)).constroi();
        Locacao locacao6 = LocacaoBuilder.umaLocacao().ativo(false).comValorAluguelSugerido(BigDecimal.valueOf(5000)).constroi();

        List<Imovel> imoveisEsperados = new ArrayList<>();
        imoveisEsperados.add(locacao1.getImovel());
        imoveisEsperados.add(locacao2.getImovel());

        locacaoRepository.salvaOuAtualiza(locacao1);
        locacaoRepository.salvaOuAtualiza(locacao2);
        locacaoRepository.salvaOuAtualiza(locacao3);
        locacaoRepository.salvaOuAtualiza(locacao4);
        locacaoRepository.salvaOuAtualiza(locacao5);
        locacaoRepository.salvaOuAtualiza(locacao6);

        manager.clear();

        List<Imovel> imoveisAtuais = locacaoRepository.buscaImoveisDisponiveisPor(BigDecimal.valueOf(1500));
        assertNotNull(imoveisAtuais);
        Matchers.arrayContainingInAnyOrder(imoveisEsperados).matches(imoveisAtuais);
    }
}
