package com.locadora.service;

import com.locadora.dao.GenericDAO;
import com.locadora.jpa.JPAUtil;
import com.locadora.model.Veiculo;

import jakarta.persistence.EntityManager;
import java.util.List;

public class VeiculoService {

    private final GenericDAO<Veiculo> dao = new GenericDAO<>(Veiculo.class);

    public Veiculo save(Veiculo obj) {
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

    public List<Veiculo> listAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return dao.listAll(em);
        } finally {
            em.close();
        }
    }

    public Veiculo findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return dao.findById(em, id);
        } finally {
            em.close();
        }
    }
}
