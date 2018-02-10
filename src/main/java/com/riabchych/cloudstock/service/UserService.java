package com.riabchych.cloudstock.service;

import com.riabchych.cloudstock.dao.IUserDAO;
import com.riabchych.cloudstock.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserDAO userDAO;

    @Override
    public synchronized boolean addUser(User user) {
        if (userDAO.userExists(user.getUsername())) {
            return false;
        } else {
            userDAO.addUser(user);
            return true;
        }
    }

    @Override
    public User getUserById(long id) {
        return userDAO.getUserById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    @Override
    public void updateUser(User user) {
        userDAO.updateUser(user);
    }

    @Override
    public void deleteUser(Long id) {
        userDAO.deleteUser(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
}
