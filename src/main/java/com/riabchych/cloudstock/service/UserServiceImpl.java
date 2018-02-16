package com.riabchych.cloudstock.service;

import com.riabchych.cloudstock.dao.UserDaoImpl;
import com.riabchych.cloudstock.entity.User;
import com.riabchych.cloudstock.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDaoImpl userDao;
    private final PasswordUtils passwordUtils;

    @Autowired
    public UserServiceImpl(UserDaoImpl userDao, PasswordUtils passwordUtils) {
        this.userDao = userDao;
        this.passwordUtils = passwordUtils;
    }

    @Override
    public synchronized boolean addUser(User user) {
        if (userDao.userExists(user.getUsername())) {
            return false;
        } else {
            //user.setRoles(new HashSet<>(roleRepository.findAll()));
            user.setSalt(passwordUtils.getSalt(12));
            user.setPassword(passwordUtils.generate(user.getPassword(),user.getSalt()));
            user.setLastPasswordResetDate(new Date());
            userDao.addUser(user);
            return true;
        }
    }

    @Override
    public User getUserById(long id) {
        return userDao.getUserById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    @Override
    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
}
