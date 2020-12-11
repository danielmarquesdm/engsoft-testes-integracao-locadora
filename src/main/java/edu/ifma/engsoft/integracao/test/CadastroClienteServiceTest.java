package edu.ifma.engsoft.integracao.test;

import edu.ifma.engsoft.integracao.model.Cliente;
import edu.ifma.engsoft.integracao.service.CadastroClienteService;
import edu.ifma.engsoft.integracao.util.exception.LocacaoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDate;

public class CadastroClienteServiceTest {
    private CadastroClienteService cadastroClienteService;
    private Cliente cliente;

    @Before
    public void setUp() {
        cadastroClienteService = new CadastroClienteService();
        cliente = new Cliente();
        cliente.setIdCliente(5L);
        cliente.setNomeCliente("Regina");
        cliente.setCpf("234.543.343-90");
        cliente.setCelular(988569865);
        cliente.setEmail("regina@email.com");
        cliente.setDtNascimento(LocalDate.of(1990, 10, 10));

    }

    @Test
    public void naoDeveSalvarClienteExistente() throws LocacaoException {
        cadastroClienteService.salvaOuAtualiza(cliente);
        Assert.assertThrows(LocacaoException.class, () -> cadastroClienteService.salvaOuAtualiza(cliente));
    }

    @Test
    public void deveSalvarCliente() throws LocacaoException {
        cliente = new Cliente();
        cliente.setIdCliente(1L);
        cliente.setNomeCliente("Regis");
        cliente.setCpf("234.943.312-80");
        cliente.setCelular(982356465);
        cliente.setEmail("regis@email.com");
        cliente.setDtNascimento(LocalDate.of(1993, 1, 10));
        Cliente clienteSalvo = cadastroClienteService.salvaOuAtualiza(this.cliente);
        Assert.assertEquals(this.cliente.getCpf(), clienteSalvo.getCpf());
    }

    @Test
    public void deveRetornarCliente() throws LocacaoException {
        Cliente clienteExistente = cadastroClienteService.buscaPor(cliente.getIdCliente());
        Assert.assertEquals(cliente.getIdCliente(), clienteExistente.getIdCliente());
    }

    @Test
    @Ignore
    public void deveRemoverCliente() throws LocacaoException {
        Cliente removido = cadastroClienteService.buscaPor(cliente.getIdCliente());
        cadastroClienteService.remove(cliente);
        Assert.assertNull(cadastroClienteService.buscaPor(removido.getIdCliente()));
    }
}
