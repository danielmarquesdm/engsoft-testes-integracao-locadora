package edu.ifma.engsoft.integracao.test;

import edu.ifma.engsoft.integracao.model.Cliente;

import java.time.LocalDate;

public class ClienteRepositoryTest {
    public static void main(String[] args) {
        Cliente cliente = new Cliente();
        cliente.setNomeCliente("Daniel");
        cliente.setCelular(98654893);
        cliente.setCpf("413.413.413-41");
        cliente.setDtNascimento(LocalDate.of(2020,12,01));
        cliente.setEmail("daniel@email.com");
    }
}
