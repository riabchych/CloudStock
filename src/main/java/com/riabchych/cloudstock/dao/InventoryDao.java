package com.riabchych.cloudstock.dao;

import com.riabchych.cloudstock.entity.Barcode;
import com.riabchych.cloudstock.entity.Inventory;

import java.util.List;

public interface InventoryDao {
    void addInventory(Inventory Inventory);

    Inventory getInventoryById(long id);

    Inventory getInventoryByBarcode(Barcode barcode);

    Inventory getInventoryByNumber(String number);

    void updateInventory(Inventory Inventory);

    void deleteInventory(Long id);

    List<Inventory> getAllInventories();

    boolean inventoryExists(String code);
}
