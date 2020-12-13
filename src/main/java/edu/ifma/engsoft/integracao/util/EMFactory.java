package edu.ifma.engsoft.integracao.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EMFactory {
    private static final EntityManagerFactory factory =
            Persistence.createEntityManagerFactory("locadora_test");

    public EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    public void close() {
        factory.close();
    }
}
