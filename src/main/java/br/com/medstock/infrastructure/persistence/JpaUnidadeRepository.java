package br.com.medstock.infrastructure.persistence;

import br.com.medstock.domain.model.Unidade;
import br.com.medstock.domain.repository.UnidadeRepository;
import br.com.medstock.infrastructure.config.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class JpaUnidadeRepository implements UnidadeRepository {

    @Override
    public Unidade save(Unidade unidade) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Unidade saved = em.merge(unidade);
            tx.commit();
            return saved;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Falha ao salvar unidade", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Unidade> findById(Integer id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Unidade.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Unidade> findByNome(String nome) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Unidade> query = em.createQuery(
                    "SELECT u FROM Unidade u WHERE u.nome = :nome", Unidade.class);
            query.setParameter("nome", nome);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Unidade> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String jpql = "SELECT u FROM Unidade u";
            return em.createQuery(jpql, Unidade.class).getResultList();
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
            Unidade unidade = em.find(Unidade.class, id);
            if (unidade != null) {
                em.remove(unidade);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Falha ao deletar unidade", e);
        } finally {
            em.close();
        }
    }
}