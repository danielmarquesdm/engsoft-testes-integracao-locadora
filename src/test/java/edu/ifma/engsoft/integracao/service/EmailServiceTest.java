package edu.ifma.engsoft.integracao.service;

import edu.ifma.engsoft.integracao.builder.AluguelBuilder;
import edu.ifma.engsoft.integracao.builder.ClienteBuilder;
import edu.ifma.engsoft.integracao.builder.LocacaoBuilder;
import edu.ifma.engsoft.integracao.model.Aluguel;
import edu.ifma.engsoft.integracao.model.Cliente;
import edu.ifma.engsoft.integracao.model.Locacao;
import edu.ifma.engsoft.integracao.repository.AluguelRepository;
import edu.ifma.engsoft.integracao.repository.LocacaoRepository;
import edu.ifma.engsoft.integracao.util.exception.LocacaoException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class EmailServiceTest {
    private EmailService emailService;
    private LocacaoRepository repository;
    private PagamentoService pagamentoService;
    private AluguelRepository aluguelRepository;

    @Before
    public void setUp() {
        emailService = new EmailService();
        pagamentoService = mock(PagamentoService.class);
        repository = mock(LocacaoRepository.class);
        aluguelRepository = mock(AluguelRepository.class);
    }

    @Test
    public void deveEnviarEmailParaClientesEmAtraso() throws LocacaoException {
        Cliente daniel = ClienteBuilder.umCliente().comNome("Daniel").constroi();
        Cliente katarina = ClienteBuilder.umCliente().comNome("Katarina").constroi();
        Cliente mario = ClienteBuilder.umCliente().comNome("Mario").constroi();
        Cliente andressa = ClienteBuilder.umCliente().comNome("Andressa").constroi();

        List<Locacao> atrasados = Arrays.asList(
                LocacaoBuilder.umaLocacao().paraUmCliente("Daniel").constroi(),
                LocacaoBuilder.umaLocacao().paraUmCliente("Katarina").constroi()
        );

        when(repository.emAtraso())
                .thenReturn(atrasados);

        emailService.enviaEmailParaClientesEmAtraso();

        verify(emailService, times(1)).notificaCliente(daniel);
        verify(emailService, times(1)).notificaCliente(katarina);
        verify(emailService, never()).notificaCliente(mario);
        verify(emailService, never()).notificaCliente(andressa);
        verifyNoMoreInteractions(emailService);
    }




}
