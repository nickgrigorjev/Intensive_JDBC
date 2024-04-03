package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static final String CREATE_TABLE = """
                CREATE TABLE IF NOT EXISTS users
                (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(128) NOT NULL,
                    last_name VARCHAR(128) NOT NULL,
                    age SMALLINT CHECK (age > 0)
                )
                """;

    @Override
    public void createUsersTable() {
        try (Session session = Util.connectionOpenHibernate()) {
            session.beginTransaction();
            session.createNativeQuery(CREATE_TABLE).executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.connectionOpenHibernate()) {
            session.beginTransaction();
            session.createNativeQuery("DROP TABLE IF EXISTS users").executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.connectionOpenHibernate()) {
            session.beginTransaction();
            session.save(new User(name,lastName,age));
            session.getTransaction().commit();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.connectionOpenHibernate()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users;
        try (Session session = Util.connectionOpenHibernate()) {
            session.beginTransaction();
            users = new ArrayList<>(session.createQuery("SELECT u FROM User u", User.class).getResultList());
            session.getTransaction().commit();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.connectionOpenHibernate()) {
            session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE users").executeUpdate();
            session.getTransaction().commit();
        }
    }
}
