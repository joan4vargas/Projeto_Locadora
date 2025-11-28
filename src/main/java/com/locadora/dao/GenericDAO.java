package com.locadora.dao;

import jakarta.persistence.EntityManager;
import java.util.List;

public class GenericDAO<T> {

    private final Class<T> clazz;

    public GenericDAO(Class<T> clazz) {
        this.clazz = clazz;
    }

    // =====================================================
    // SAVE (CREATE or UPDATE)
    // =====================================================
    public T save(EntityManager em, T entity) {
        em.getTransaction().begin();
        T managed = em.merge(entity);
        em.getTransaction().commit();
        return managed;
    }

    // =====================================================
    // DELETE
    // =====================================================
    public void delete(EntityManager em, Long id) {
        em.getTransaction().begin();
        T ref = em.find(clazz, id);
        if (ref != null) {
            em.remove(ref);
        }
        em.getTransaction().commit();
    }

    // =====================================================
    // LIST ALL
    // =====================================================
    public List<T> listAll(EntityManager em) {
        return em
                .createQuery("from " + clazz.getSimpleName(), clazz)
                .getResultList();
    }

    // =====================================================
    // FIND BY ID
    // =====================================================
    public T findById(EntityManager em, Long id) {
        return em.find(clazz, id);
    }
}
