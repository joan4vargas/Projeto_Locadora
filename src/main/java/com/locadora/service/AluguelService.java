package com.locadora.service;

import com.locadora.dao.GenericDAO;
import com.locadora.jpa.JPAUtil;
import com.locadora.model.Aluguel;

import jakarta.persistence.EntityManager;
import java.util.List;

public class AluguelService {

    private final GenericDAO<Aluguel> dao = new GenericDAO<>(Aluguel.class);

    public Aluguel save(Aluguel obj) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return dao.save(em, obj);
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            dao.delete(em, id);
        } finally {
            em.close();
        }
    }

    public List<Aluguel> listAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return dao.listAll(em);
        } finally {
            em.close();
        }
    }

    public Aluguel findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return dao.findById(em, id);
        } finally {
            em.close();
        }
    }
}
