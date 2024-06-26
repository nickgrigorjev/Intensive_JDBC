package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args)  {

        UserServiceImpl userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Ivan","Ivanov", (byte) 25);
        userService.saveUser("Semen","Semenov", (byte) 26);
        userService.saveUser("Petr","Petrov", (byte) 27);
        userService.saveUser("Boris","Borisov", (byte) 28);
        userService.getAllUsers();
        userService.removeUserById(1);
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
