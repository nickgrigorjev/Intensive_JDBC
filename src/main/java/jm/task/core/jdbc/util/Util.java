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
    //TODO 02.04.2024 - 10:04: реализуйте настройку соеденения с БД

    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";

    private static final Properties PROPERTIES = new Properties();

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        try (InputStream inputStream = Util.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static Connection connectionOpen() {
        try {
            return DriverManager.getConnection(
                    PROPERTIES.getProperty(URL_KEY),
                    PROPERTIES.getProperty(USERNAME_KEY),
                    PROPERTIES.getProperty(PASSWORD_KEY)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Session connectionOpenHibernate() {
        try {
            SessionFactory sessionFactory = new Configuration()
                .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                .setProperty("hibernate.connection.url", PROPERTIES.getProperty(URL_KEY))
                .setProperty("hibernate.connection.username", PROPERTIES.getProperty(USERNAME_KEY))
                .setProperty("hibernate.connection.password", PROPERTIES.getProperty(PASSWORD_KEY))
                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
            Session session = sessionFactory.openSession();
            return session;
        } catch (HibernateException e) {
            throw new RuntimeException();
        }
    }
}
