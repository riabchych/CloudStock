package com.riabchych.cloudstock.dao;

import com.riabchych.cloudstock.entity.Role;

import java.util.List;

public interface RoleDAO {
    void addRole(Role role);

    Role getRoleById(long id);

    Role getRoleByName(String name);

    void updateRole(Role role);

    void deleteRole(Long id);

    List<Role> getAllRoles();

    boolean roleExists(String name);
}
