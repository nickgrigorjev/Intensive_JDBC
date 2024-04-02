package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String CREATE_TABLE = """
                CREATE TABLE IF NOT EXISTS users
                (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(128) NOT NULL,
                    last_name VARCHAR(128) NOT NULL,
                    age SMALLINT CHECK (age > 0)
                )
                """;
        try (Connection connection = Util.connectionOpen();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TABLE)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        String DROP_TABLE = """
                DROP TABLE IF EXISTS users
                """;
        try (Connection connection = Util.connectionOpen();
             PreparedStatement preparedStatement = connection.prepareStatement(DROP_TABLE)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String SAVE_USER = """
                INSERT INTO users (name, last_name, age) 
                VALUES (?,?,?)
                """;
        try (Connection connection = Util.connectionOpen();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_USER)) {
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,lastName);
            preparedStatement.setByte(3,age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        String DELETE_USER = """
                DELETE FROM users
                WHERE id = ?
                """;
        try (Connection connection = Util.connectionOpen();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String GET_ALL_USERS = """
                SELECT * FROM users
                """;
        try (Connection connection = Util.connectionOpen();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_USERS)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    public void cleanUsersTable() {
        String CLEAN_TABLE = """
                TRUNCATE TABLE users
                """;
        try (Connection connection = Util.connectionOpen();
             PreparedStatement preparedStatement = connection.prepareStatement(CLEAN_TABLE)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
