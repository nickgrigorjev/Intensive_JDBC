package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Util {
    private Util() {
    }

    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";

    private static final Properties PROPERTIES = new Properties();


    static {
        try (InputStream inputStream = Util.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Connection connectionOpen() {
        Connection connection = null;
        try {
            return DriverManager.getConnection(
                    PROPERTIES.getProperty(URL_KEY),
                    PROPERTIES.getProperty(USERNAME_KEY),
                    PROPERTIES.getProperty(PASSWORD_KEY)
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static Session connectionOpenHibernate() {
        Session session = null;
        try {
            SessionFactory sessionFactory = new Configuration()
                    .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                    .setProperty("hibernate.connection.url", PROPERTIES.getProperty(URL_KEY))
                    .setProperty("hibernate.connection.username", PROPERTIES.getProperty(USERNAME_KEY))
                    .setProperty("hibernate.connection.password", PROPERTIES.getProperty(PASSWORD_KEY))
                    .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
            return sessionFactory.openSession();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return session;
    }
}
