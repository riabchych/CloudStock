package com.riabchych.cloudstock.dao;

import com.riabchych.cloudstock.entity.Barcode;

import java.util.List;

public interface IBarcodeDAO {
    void addBarcode(Barcode Barcode);

    Barcode getBarcodeById(long id);

    void updateBarcode(Barcode Barcode);

    void deleteBarcode(Long id);

    List<Barcode> getAllBarcodes();

    boolean barcodeExists(String code);
}
