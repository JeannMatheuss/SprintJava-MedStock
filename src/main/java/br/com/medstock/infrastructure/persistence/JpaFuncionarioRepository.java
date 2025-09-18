package br.com.medstock.infrastructure.persistence;

import br.com.medstock.domain.model.Funcionario;
import br.com.medstock.domain.repository.FuncionarioDAO;
import br.com.medstock.infrastructure.config.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class JpaFuncionarioRepository implements FuncionarioDAO {

    @Override
    public Funcionario save(Funcionario funcionario) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Funcionario saved = em.merge(funcionario);
            tx.commit();
            return saved;
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Falha ao salvar funcionário", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Funcionario> findById(Integer id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return Optional.ofNullable(em.find(Funcionario.class, id));
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Funcionario> findByMatricula(String matricula) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Funcionario> query = em.createQuery(
                    "SELECT f FROM Funcionario f WHERE f.matricula = :matricula", Funcionario.class);
            query.setParameter("matricula", matricula);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Funcionario> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String jpql = "SELECT f FROM Funcionario f";
            return em.createQuery(jpql, Funcionario.class).getResultList();
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
            Funcionario funcionario = em.find(Funcionario.class, id);
            if (funcionario != null) {
                em.remove(funcionario);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Falha ao deletar funcionário", e);
        } finally {
            em.close();
        }
    }
}