package com.riabchych.cloudstock.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "barcodes")
public class Barcode implements Serializable {

    @Id
    @Column()
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column()
    private String code;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "barcode")
    private Inventory inventory;

    public Barcode() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}