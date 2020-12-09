package edu.ifma.engsoft.integracao.test;

import edu.ifma.engsoft.integracao.model.Cliente;
import edu.ifma.engsoft.integracao.model.Imovel;
import edu.ifma.engsoft.integracao.model.Locacao;
import edu.ifma.engsoft.integracao.util.EMFactory;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

public class LocacaoRepositoryTest {
    public static void main(String[] args) {
        EMFactory factory = new EMFactory();
        EntityManager manager = factory.getEntityManager();
        manager.getTransaction().begin();

        Imovel imovel = manager.find(Imovel.class, 2);
        Cliente cliente = manager.find(Cliente.class, 4);
        Locacao locacao = new Locacao(imovel, cliente);
        locacao.setValorAluguel(BigDecimal.valueOf(1390));

        manager.persist(locacao);
        manager.getTransaction().commit();
        manager.close();
        factory.close();
    }
}
