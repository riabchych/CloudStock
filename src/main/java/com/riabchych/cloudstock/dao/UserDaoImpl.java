package com.riabchych.cloudstock.dao;

import com.riabchych.cloudstock.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Transactional
@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public User getUserById(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getUserByUsername(String username) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> rootEntry = cq.from(User.class);

        CriteriaQuery<User> codes = cq.where(cb.equal(rootEntry.get("username"), username));
        TypedQuery<User> allQuery = entityManager.createQuery(codes).setMaxResults(1);

        return (User) allQuery.getSingleResult();
    }

    @Override
    public void updateUser(User user) {
        User barc = getUserById(user.getId());
        barc.setName(user.getName());
        barc.setOwnedItems(user.getOwnedItems());
        barc.setRoles(user.getRoles());
        barc.setUsedItems(user.getUsedItems());
        entityManager.flush();
    }

    @Override
    public void deleteUser(Long id) {
        entityManager.remove(getUserById(id));
    }

    @Override
    public List<User> getAllUsers() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> rootEntry = cq.from(User.class);
        CriteriaQuery<User> all = cq.select(rootEntry);
        TypedQuery<User> allQuery = entityManager.createQuery(all);
        return allQuery.getResultList();
    }

    @Override
    public boolean userExists(String username) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> rootEntry = cq.from(User.class);

        CriteriaQuery<User> codes = cq.where(cb.equal(rootEntry.get("username"), username));
        TypedQuery<User> allQuery = entityManager.createQuery(codes);

        return allQuery.getResultList().size() > 0;
    }
} 