package com.riabchych.cloudstock.dao;

import com.riabchych.cloudstock.entity.Barcode;
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
public class BarcodeDAO implements IBarcodeDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addBarcode(Barcode barcode) {
        entityManager.persist(barcode);
    }

    @Override
    public Barcode getBarcodeById(long id) {
        return entityManager.find(Barcode.class, id);
    }

    @Override
    public void updateBarcode(Barcode barcode) {
        Barcode barc = getBarcodeById(barcode.getId());
        barc.setCode(barcode.getCode());
        barc.setInventory(barcode.getInventory());
        entityManager.flush();
    }

    @Override
    public void deleteBarcode(Long id) {
        entityManager.remove(getBarcodeById(id));
    }

    @Override
    public List<Barcode> getAllBarcodes() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Barcode> cq = cb.createQuery(Barcode.class);
        Root<Barcode> rootEntry = cq.from(Barcode.class);
        CriteriaQuery<Barcode> all = cq.select(rootEntry);
        TypedQuery<Barcode> allQuery = entityManager.createQuery(all);
        return allQuery.getResultList();
    }

    @Override
    public boolean barcodeExists(String code) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Barcode> cq = cb.createQuery(Barcode.class);
        Root<Barcode> rootEntry = cq.from(Barcode.class);

        CriteriaQuery<Barcode> codes = cq.where(cb.equal(rootEntry.get("code"), code));
        TypedQuery<Barcode> allQuery = entityManager.createQuery(codes);

        return allQuery.getResultList().size() > 0;
    }
} 