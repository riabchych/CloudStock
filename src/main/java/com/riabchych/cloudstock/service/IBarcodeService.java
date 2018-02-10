package com.riabchych.cloudstock.service;

import com.riabchych.cloudstock.entity.Barcode;

import java.util.List;

public interface IBarcodeService {

    boolean addBarcode(Barcode barcode);

    Barcode getBarcodeById(long id);

    void updateBarcode(Barcode barcode);

    void deleteBarcode(Long id);

    List<Barcode> getAllBarcodes();
}
