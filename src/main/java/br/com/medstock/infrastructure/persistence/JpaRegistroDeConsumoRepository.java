package br.com.medstock.infrastructure.persistence;

import br.com.medstock.domain.model.RegistroDeConsumo;
import br.com.medstock.domain.repository.RegistroDeConsumoDAO;
import br.com.medstock.infrastructure.config.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class JpaRegistroDeConsumoRepository implements RegistroDeConsumoDAO {

    @Override
    public RegistroDeConsumo save(RegistroDeConsumo registro) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            RegistroDeConsumo saved = em.merge(registro);
            tx.commit();
            return saved;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Falha ao salvar registro de consumo", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<RegistroDeConsumo> findById(Integer id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(RegistroDeConsumo.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public List<RegistroDeConsumo> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String jpql = "SELECT r FROM RegistroDeConsumo r";
            return em.createQuery(jpql, RegistroDeConsumo.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<RegistroDeConsumo> findByPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String jpql = "SELECT r FROM RegistroDeConsumo r WHERE r.data >= :dataInicio AND r.data <= :dataFim";
            TypedQuery<RegistroDeConsumo> query = em.createQuery(jpql, RegistroDeConsumo.class);
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(Integer id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            RegistroDeConsumo registro = em.find(RegistroDeConsumo.class, id);
            if (registro != null) em.remove(registro);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Falha ao deletar registro de consumo", e);
        } finally {
            em.close();
        }
    }
}