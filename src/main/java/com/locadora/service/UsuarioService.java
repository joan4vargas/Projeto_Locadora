package com.locadora.service;

import com.locadora.dao.GenericDAO;
import com.locadora.jpa.JPAUtil;
import com.locadora.model.Usuario;

import jakarta.persistence.EntityManager;
import java.util.List;

public class UsuarioService {

    private final GenericDAO<Usuario> dao = new GenericDAO<>(Usuario.class);

    public Usuario save(Usuario obj) {
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

    public List<Usuario> listAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return dao.listAll(em);
        } finally {
            em.close();
        }
    }

    public Usuario findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return dao.findById(em, id);
        } finally {
            em.close();
        }
    }
}
