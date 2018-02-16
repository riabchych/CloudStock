package com.riabchych.cloudstock.service;

import com.riabchych.cloudstock.dao.BarcodeDao;
import com.riabchych.cloudstock.entity.Barcode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarcodeServiceImpl implements BarcodeService {

    private final BarcodeDao barcodeDao;

    @Autowired
    public BarcodeServiceImpl(BarcodeDao barcodeDao) {
        this.barcodeDao = barcodeDao;
    }

    @Override
    public synchronized boolean addBarcode(Barcode barcode) {
        if (barcodeDao.barcodeExists(barcode.getCode())) {
            return false;
        } else {
            barcodeDao.addBarcode(barcode);
            return true;
        }
    }

    @Override
    public Barcode getBarcodeById(long id) {
        return barcodeDao.getBarcodeById(id);
    }

    @Override
    public void updateBarcode(Barcode barcode) {
        barcodeDao.updateBarcode(barcode);
    }

    @Override
    public void deleteBarcode(Long id) {
        barcodeDao.deleteBarcode(id);
    }

    @Override
    public List<Barcode> getAllBarcodes() {
        return barcodeDao.getAllBarcodes();
    }
}
