package br.com.medstock.infrastructure.persistence;

import br.com.medstock.domain.model.Material;
import br.com.medstock.domain.repository.MaterialRepository;
import br.com.medstock.infrastructure.config.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class JpaMaterialRepository implements MaterialRepository {

    @Override
    public Material save(Material material) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Material saved = em.merge(material);
            tx.commit();
            return saved;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Falha ao salvar material", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Material> findById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Material.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public List<Material> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String jpql = "SELECT m FROM Material m";
            return em.createQuery(jpql, Material.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Material> findByEstoqueAbaixoDe(int limite) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String jpql = "SELECT m FROM Material m WHERE m.quantidadeDisponivel <= :limite";
            TypedQuery<Material> query = em.createQuery(jpql, Material.class);
            query.setParameter("limite", limite);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(int id) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Material material = em.find(Material.class, id);
            if (material != null) {
                em.remove(material);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Falha ao deletar material", e);
        } finally {
            em.close();
        }
    }
}