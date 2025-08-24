package br.com.medstock.application;

import br.com.medstock.infrastructure.config.JpaUtil;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

class JpaUtilTest {

    @Test
    void testGetEntityManager() {
        EntityManager em = JpaUtil.getEntityManager();
        assertNotNull(em);
        assertTrue(em.isOpen());
        em.close();
    }

    @Test
    void testShutdown() {
        JpaUtil.shutdown();
        assertTrue(true);
    }
}
