package com.riabchych.cloudstock.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User implements Serializable {

    @Id
    @Column()
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column()
    private String name;

    @Column(columnDefinition = "TINYINT(1)")
    private int isOwner;

    @Column()
    private String password;

    @Column()
    private String username;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Inventory> usedItems;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Set<Inventory> ownedItems;
    private int version = 0;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
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

    public String[] getRolesString() {
        return roles.stream().map(Role::getRole).toArray(String[]::new);
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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

    public int getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(int isOwner) {
        this.isOwner = isOwner;
    }

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
}