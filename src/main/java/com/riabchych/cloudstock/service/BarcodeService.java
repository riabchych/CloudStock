package com.riabchych.cloudstock.service;

import com.riabchych.cloudstock.dao.IBarcodeDAO;
import com.riabchych.cloudstock.entity.Barcode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarcodeService implements IBarcodeService {

    @Autowired
    private IBarcodeDAO barcodeDAO;

    @Override
    public synchronized boolean addBarcode(Barcode barcode) {
        if (barcodeDAO.barcodeExists(barcode.getCode())) {
            return false;
        } else {
            barcodeDAO.addBarcode(barcode);
            return true;
        }
    }

    @Override
    public Barcode getBarcodeById(long id) {
        return barcodeDAO.getBarcodeById(id);
    }

    @Override
    public void updateBarcode(Barcode barcode) {
        barcodeDAO.updateBarcode(barcode);
    }

    @Override
    public void deleteBarcode(Long id) {
        barcodeDAO.deleteBarcode(id);
    }

    @Override
    public List<Barcode> getAllBarcodes() {
        return barcodeDAO.getAllBarcodes();
    }
}
