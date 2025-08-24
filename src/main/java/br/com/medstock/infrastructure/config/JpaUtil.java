package br.com.medstock.infrastructure.config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class JpaUtil {

    private static EntityManagerFactory FACTORY;

    private static Function<String, String> envProvider = System::getenv;

    private JpaUtil() {}

    public static void setEnvProvider(Function<String, String> provider) {
        envProvider = provider;
    }

    private static synchronized EntityManagerFactory getFactory() {
        if (FACTORY == null) {
            try {
                Map<String, String> properties = new HashMap<>();

                String dbHost = envProvider.apply("DB_HOST");
                String dbName = envProvider.apply("DB_NAME");
                String dbUser = envProvider.apply("DB_USER");
                String dbPassword = envProvider.apply("DB_PASSWORD");

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
        return FACTORY;
    }

    public static EntityManager getEntityManager() {
        return getFactory().createEntityManager();
    }

    public static void shutdown() {
        if (FACTORY != null && FACTORY.isOpen()) {
            FACTORY.close();
        }
    }
}