package edu.ifma.engsoft.integracao.repository;

import edu.ifma.engsoft.integracao.builder.ClienteBuilder;
import edu.ifma.engsoft.integracao.model.Cliente;
import edu.ifma.engsoft.integracao.service.ClienteService;
import edu.ifma.engsoft.integracao.util.EMFactory;
import edu.ifma.engsoft.integracao.util.exception.LocacaoException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import javax.persistence.EntityManager;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ClienteRepositoryTest {
    private EntityManager manager;
    private static EMFactory factory;
    private ClienteRepository clienteRepository;
    private ClienteService clienteService;
    private Cliente cliente;

    @Before
    public void setUp() {
        manager = new EMFactory().getEntityManager();
        manager.getTransaction().begin();
        clienteRepository = new ClienteRepository(manager);
        clienteService = new ClienteService(clienteRepository);
        cliente = ClienteBuilder.umCliente().constroi();
//        cliente.setIdCliente(1L);
//        cliente.setNomeCliente("Regis");
//        cliente.setCpf("234.943.312-80");
//        cliente.setCelular(982356465);
//        cliente.setEmail("regis@email.com");
//        cliente.setDtNascimento(LocalDate.of(1993, 1, 10));
    }

    @AfterEach
    public void after() {
        manager.getTransaction().rollback();
    }

    @AfterAll
    public static void end() {
        factory.close();
    }

    @Test(expected = LocacaoException.class)
    public void naoDeveSalvarClienteExistente() throws LocacaoException {
        Assertions.assertThrows(LocacaoException.class,
                () -> clienteService.salvaOuAtualiza(cliente),
                "Usuário já existe.");

        clienteService.salvaOuAtualiza(cliente);
        manager.flush();
        manager.clear();

        Cliente usuarioDoBanco = clienteRepository.buscaPor(cliente.getIdCliente());

        assertThat(cliente.getIdCliente(),
                is(equalTo(usuarioDoBanco.getIdCliente())));
    }

    @Test
    @Ignore
    //TODO INSERIR DADOS DE UM CLIENTE NOVO
    public void deveSalvarCliente() throws LocacaoException {
        ClienteBuilder.umCliente().comNome("Katia");
        Cliente clienteSalvo = clienteService.salvaOuAtualiza(cliente);
        Assertions.assertEquals(cliente, clienteSalvo);
    }

    @Test
    public void deveEncontrarCliente() {
        Cliente clienteDoBanco = clienteRepository.buscaPor(this.cliente.getIdCliente());
        Assertions.assertNotNull(clienteDoBanco);
    }

}
