package com.riabchych.cloudstock.dao;

import com.riabchych.cloudstock.entity.User;

import java.util.List;

public interface UserDao {
    void addUser(User user);

    User getUserById(long id);

    User getUserByUsername(String username);

    void updateUser(User user);

    void deleteUser(Long id);

    List<User> getAllUsers();

    boolean userExists(String username);
}
