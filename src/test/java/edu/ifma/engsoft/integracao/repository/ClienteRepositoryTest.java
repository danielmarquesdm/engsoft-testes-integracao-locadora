package edu.ifma.engsoft.integracao.repository;

import edu.ifma.engsoft.integracao.builder.ClienteBuilder;
import edu.ifma.engsoft.integracao.model.Cliente;
import edu.ifma.engsoft.integracao.util.EMFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

public class ClienteRepositoryTest {
    private EntityManager manager;
    private static EMFactory factory;
    private ClienteRepository clienteRepository;
    private Cliente cliente;

    @Before
    public void setUp() {
        factory = new EMFactory();
        manager = factory.getEntityManager();
        manager.getTransaction().begin();
        clienteRepository = new ClienteRepository(manager);
        cliente = ClienteBuilder.umCliente().constroi();
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
    public void deveAtualizarCliente() {
        clienteRepository.salvaOuAtualiza(cliente);
        cliente.setNomeCliente("Margareth");
        clienteRepository.salvaOuAtualiza(cliente);

        manager.flush();
        manager.clear();

        Cliente clienteSalvo = clienteRepository.buscaPor(cliente.getNomeCliente());

        assertThat(clienteSalvo.getNomeCliente(),
                is(equalTo("Margareth")));
    }

    @Test
    public void deveSalvarCliente() {
        Cliente cliente = ClienteBuilder.umCliente().comNome("Katia").constroi();
        clienteRepository.salvaOuAtualiza(cliente);
        Cliente clienteDoBanco = clienteRepository.buscaPor("Katia");
        assertNotNull(clienteDoBanco);
    }

    @Test
    public void deveEncontrarCliente() {
        clienteRepository.salvaOuAtualiza(cliente);
        Cliente clienteDoBanco = clienteRepository.buscaPor(this.cliente.getNomeCliente());
        assertNotNull(clienteDoBanco);
        assertEquals(cliente, clienteDoBanco);
    }

    @Test
    public void deveExcluirCliente() {
        clienteRepository.remove(cliente);
        assertThrows(NoResultException.class,
                () -> clienteRepository.buscaPor(cliente.getNomeCliente()),
                "Deveria ter lançado a exceção NoResultException");
    }

}
