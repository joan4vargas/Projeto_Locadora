package com.locadora.init;

import com.locadora.jpa.JPAUtil;
import com.locadora.model.Usuario;
import com.locadora.model.Veiculo;
import com.locadora.model.Aluguel;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;

public class DataLoader {

    public static void seed() {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            long countUsuarios = (long) em.createQuery("SELECT COUNT(u) FROM Usuario u").getSingleResult();
            long countVeiculos = (long) em.createQuery("SELECT COUNT(v) FROM Veiculo v").getSingleResult();
            long countAlugueis = (long) em.createQuery("SELECT COUNT(a) FROM Aluguel a").getSingleResult();

            // ============================
            // SEED USUÁRIOS
            // ============================
            if (countUsuarios == 0) {
                em.persist(new Usuario("João Pedro", "joao@email.com", "(47) 99999-0000"));
                em.persist(new Usuario("Maria Silva", "maria@gmail.com", "(47) 98888-1234"));
                em.persist(new Usuario("Carlos Santos", "carlos@hotmail.com", "(47) 97777-8888"));
            }

            // ============================
            // SEED VEÍCULOS
            // ============================
            if (countVeiculos == 0) {
                em.persist(new Veiculo("ABC1D23", "Fiat", "Argo", 2020, "Prata"));
                em.persist(new Veiculo("BRA2E19", "Volkswagen", "Gol", 2019, "Branco"));
                em.persist(new Veiculo("XYZ1234", "Chevrolet", "Onix", 2021, "Preto"));
            }

            // ============================
            // SEED ALUGUÉIS
            // ============================
            if (countAlugueis == 0) {

                Usuario u1 = em.createQuery(
                                "FROM Usuario u WHERE u.nome = 'João Pedro'",
                                Usuario.class
                        )
                        .getResultList()
                        .stream()
                        .findFirst()
                        .orElse(null);

                Veiculo v1 = em.createQuery(
                                "FROM Veiculo v WHERE v.modelo = 'Argo'",
                                Veiculo.class
                        )
                        .getResultList()
                        .stream()
                        .findFirst()
                        .orElse(null);

                if (u1 != null && v1 != null) {
                    Aluguel a = new Aluguel(
                            LocalDate.now(),
                            LocalDate.now(),
                            "ABERTO",
                            10000,
                            u1,
                            v1
                    );
                    em.persist(a);
                }
            }

            em.getTransaction().commit();

        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();

        } finally {
            em.close();
        }
    }
}
