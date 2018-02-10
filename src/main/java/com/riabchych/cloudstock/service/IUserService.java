package com.riabchych.cloudstock.service;

import com.riabchych.cloudstock.entity.User;

import java.util.List;

public interface IUserService {
    boolean addUser(User user);

    User getUserById(long id);

    User getUserByUsername(String username);

    void updateUser(User user);

    void deleteUser(Long id);

    List<User> getAllUsers();
}
