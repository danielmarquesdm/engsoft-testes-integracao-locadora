package edu.ifma.engsoft.integracao.test;

import edu.ifma.engsoft.integracao.enums.TipoImovel;
import edu.ifma.engsoft.integracao.model.Imovel;
import edu.ifma.engsoft.integracao.util.EMFactory;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

public class ImovelRepositoryTest {
    public static void main(String[] args) {
        EMFactory factory = new EMFactory();
        EntityManager manager = factory.getEntityManager();
        manager.getTransaction().begin();

        Imovel imovel = new Imovel();
        imovel.setTipoImovel(TipoImovel.APARTAMENTO);
        imovel.setEndereco("Avenida dois, 1003");
        imovel.setBairro("Araçagy");
        imovel.setCep(65348936);
        imovel.setMetragem(102.56);
        imovel.setDormitorios(4);
        imovel.setBanheiros(3);
        imovel.setSuites(2);
        imovel.setVagasGaragem(5);
        imovel.setValorAluguelSugerido(BigDecimal.valueOf(2500));
        imovel.setObservacao("Excelente localizacao, proximo a universidade e hospital");

        manager.persist(imovel);
        manager.getTransaction().commit();
        manager.close();
        factory.close();
    }
}
