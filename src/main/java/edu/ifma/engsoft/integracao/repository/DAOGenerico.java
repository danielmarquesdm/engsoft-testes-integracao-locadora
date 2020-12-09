package edu.ifma.engsoft.integracao.repository;

import edu.ifma.engsoft.integracao.model.EntidadeBase;

import javax.persistence.EntityManager;
import java.util.Objects;

class DAOGenerico<T extends EntidadeBase> {
    private final EntityManager manager;

    DAOGenerico(EntityManager manager) {
        this.manager = manager;
    }

    T buscaPorId(Class<T> clazz, Long id) {
        return manager.find(clazz, id);
    }

    T salvaOuAtualiza(T t) {
        if (Objects.isNull(t.getId()))
            this.manager.persist(t);
        else
            t = this.manager.merge(t);
        return t;
    }

    void remove(T t) {
        if (Objects.nonNull(t.getId())) {
            manager.remove(t);
            manager.flush();
        }
    }
}
