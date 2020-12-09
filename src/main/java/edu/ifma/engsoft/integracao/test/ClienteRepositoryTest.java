package edu.ifma.engsoft.integracao.test;

import edu.ifma.engsoft.integracao.model.Cliente;
import edu.ifma.engsoft.integracao.util.EMFactory;

import javax.persistence.EntityManager;
import java.time.LocalDate;

public class ClienteRepositoryTest {
    public static void main(String[] args) {
        EMFactory factory = new EMFactory();
        EntityManager manager = factory.getEntityManager();
        manager.getTransaction().begin();
        Cliente cliente = new Cliente();
        cliente.setNomeCliente("Daniel");
        cliente.setCpf("413.413.413-41");
        cliente.setCelular(98654893);
        cliente.setEmail("daniel@email.com");
        cliente.setDtNascimento(LocalDate.of(1992, 12, 01));
        manager.persist(cliente);
        manager.getTransaction().commit();
        manager.close();
        factory.close();
    }
}
