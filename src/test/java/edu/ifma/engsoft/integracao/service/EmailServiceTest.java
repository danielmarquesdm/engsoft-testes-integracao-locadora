package edu.ifma.engsoft.integracao.service;

import edu.ifma.engsoft.integracao.builder.AluguelBuilder;
import edu.ifma.engsoft.integracao.builder.ClienteBuilder;
import edu.ifma.engsoft.integracao.builder.ImovelBuilder;
import edu.ifma.engsoft.integracao.builder.LocacaoBuilder;
import edu.ifma.engsoft.integracao.model.Aluguel;
import edu.ifma.engsoft.integracao.model.Cliente;
import edu.ifma.engsoft.integracao.model.Imovel;
import edu.ifma.engsoft.integracao.model.Locacao;
import edu.ifma.engsoft.integracao.repository.AluguelRepository;
import edu.ifma.engsoft.integracao.repository.LocacaoRepository;
import edu.ifma.engsoft.integracao.util.EMFactory;
import edu.ifma.engsoft.integracao.util.exception.LocacaoException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class EmailServiceTest {
    private EmailService emailService;
    private EMFactory factory;
    private EntityManager manager;
    private AluguelRepository aluguelRepository;

    @Before
    public void setUp() {
        emailService = new EmailService();
        factory = mock(EMFactory.class);
        manager = mock(EntityManager.class);
        aluguelRepository = mock(AluguelRepository.class);
    }

    @Test
    public void deveEnviarEmailParaClientesEmAtraso() throws LocacaoException {
        Cliente daniel = ClienteBuilder.umCliente().comId(1L).comNome("Daniel").constroi();
        Cliente alex = ClienteBuilder.umCliente().comId(2L).comNome("Alex").constroi();
        Cliente mario = ClienteBuilder.umCliente().comId(3L).comNome("Mario").constroi();

        Imovel imovel1 = ImovelBuilder.umImovel().comId(1L).constroi();
        Imovel imovel2 = ImovelBuilder.umImovel().comId(2L).constroi();
        Imovel imovel3 = ImovelBuilder.umImovel().comId(3L).constroi();

        Locacao locacao1 = LocacaoBuilder.umaLocacao().comId(1L).paraUmCliente(daniel).paraUmImovel(imovel1).constroi();
        Locacao locacao2 = LocacaoBuilder.umaLocacao().comId(2L).paraUmCliente(alex).paraUmImovel(imovel2).constroi();
        Locacao locacao3 = LocacaoBuilder.umaLocacao().comId(3L).paraUmCliente(mario).paraUmImovel(imovel3).constroi();

        Aluguel aluguel1 = AluguelBuilder.umAluguel().comId(1L).paraUmCliente("Daniel")
                .paraUmaLocacao(locacao1)
                .comDataDePagamento(LocalDate.of(2020, 12, 10))
                .comDataDeVencimento(LocalDate.of(2020, 12, 5))
                .constroi();
        Aluguel aluguel2 = AluguelBuilder.umAluguel().comId(2L).paraUmCliente("Alex")
                .paraUmaLocacao(locacao2)
                .comDataDePagamento(LocalDate.of(2020, 12, 10))
                .comDataDeVencimento(LocalDate.of(2020, 12, 5))
                .constroi();
        Aluguel aluguel3 = AluguelBuilder.umAluguel().comId(3L).paraUmCliente("Mario")
                .paraUmaLocacao(locacao3)
                .comDataDePagamento(LocalDate.of(2020, 12, 10))
                .comDataDeVencimento(LocalDate.of(2020, 12, 15))
                .constroi();

        aluguelRepository.salvaOuAtualiza(aluguel1);
        aluguelRepository.salvaOuAtualiza(aluguel2);
        aluguelRepository.salvaOuAtualiza(aluguel3);

        List<Aluguel> alugueisAtrasados = new ArrayList<>();
        alugueisAtrasados.add(aluguel1);
        alugueisAtrasados.add(aluguel2);

        when(aluguelRepository.emAtraso())
                .thenReturn(alugueisAtrasados);

        boolean verdadeiro = emailService.enviaEmailParaClientesEmAtraso();

        verify(aluguelRepository, times(3)).salvaOuAtualiza(any());
        Assertions.assertTrue(verdadeiro);
    }


}
