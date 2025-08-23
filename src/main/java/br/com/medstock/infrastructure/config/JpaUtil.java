package br.com.medstock.infrastructure.config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public final class JpaUtil {

    private static final EntityManagerFactory FACTORY;

    static {
        try {
            Map<String, String> properties = new HashMap<>();

            String dbHost = System.getenv("DB_HOST");
            String dbName = System.getenv("DB_NAME");
            String dbUser = System.getenv("DB_USER");
            String dbPassword = System.getenv("DB_PASSWORD");

            if (dbHost == null || dbName == null || dbUser == null || dbPassword == null) {
                throw new IllegalStateException("As variáveis de ambiente DB_HOST, DB_NAME, DB_USER e DB_PASSWORD devem ser configuradas.");
            }

            String jdbcUrl = String.format("jdbc:oracle:thin:@%s:1521:%s", dbHost, dbName);

            properties.put("javax.persistence.jdbc.url", jdbcUrl);
            properties.put("javax.persistence.jdbc.user", dbUser);
            properties.put("javax.persistence.jdbc.password", dbPassword);

            FACTORY = Persistence.createEntityManagerFactory("medstock-pu", properties);
        } catch (Exception e) {
            System.err.println("Falha ao inicializar a fábrica de EntityManager do JPA.");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private JpaUtil() {
    }

    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }

    public static void shutdown() {
        if (FACTORY != null && FACTORY.isOpen()) {
            FACTORY.close();
        }
    }
}