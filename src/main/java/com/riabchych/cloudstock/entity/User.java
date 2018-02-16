package com.riabchych.cloudstock.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @JsonIgnore
    @Column(name = "salt")
    private String salt;

    @Column(name = "username")
    private String username;

    @Column(name = "password_reset_date")
    private Date lastPasswordResetDate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private Set<Inventory> usedItems;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    private Set<Inventory> ownedItems;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @JsonIgnore
    public String[] getRolesString() {
        return roles.stream().map(role -> role.getName().toString()).toArray(String[]::new);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Inventory> getUsedItems() {
        return usedItems;
    }

    public void setUsedItems(Set<Inventory> usedItems) {
        this.usedItems = usedItems;
    }

    public Set<Inventory> getOwnedItems() {
        return ownedItems;
    }

    public void setOwnedItems(Set<Inventory> ownedItems) {
        this.ownedItems = ownedItems;
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    @JsonIgnore
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}