package edu.ifma.engsoft.integracao;

import edu.ifma.engsoft.integracao.util.EMFactory;

import javax.persistence.EntityManager;

public class App {
    public static void main(String[] args) {
        EMFactory factory = new EMFactory();
        EntityManager manager = factory.getEntityManager();

        manager.getTransaction().begin();
        manager.getTransaction().commit();
        factory.close();
    }
}
