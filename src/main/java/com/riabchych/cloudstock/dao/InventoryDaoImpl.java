package com.riabchych.cloudstock.dao;

import com.riabchych.cloudstock.entity.Barcode;
import com.riabchych.cloudstock.entity.Inventory;

import java.util.List;

public class InventoryDaoImpl implements InventoryDao {
    @Override
    public void addInventory(Inventory Inventory) {
        // TODO: Implement method
    }

    @Override
    public Inventory getInventoryById(long id) {
        // TODO: Implement method
        return null;
    }

    @Override
    public Inventory getInventoryByBarcode(Barcode barcode) {
        // TODO: Implement method
        return null;
    }

    @Override
    public Inventory getInventoryByNumber(String number) {
        // TODO: Implement method
        return null;
    }

    @Override
    public void updateInventory(Inventory Inventory) {
        // TODO: Implement method
    }

    @Override
    public void deleteInventory(Long id) {
        // TODO: Implement method
    }

    @Override
    public List<Inventory> getAllInventories() {
        // TODO: Implement method
        return null;
    }

    @Override
    public boolean inventoryExists(String code) {
        // TODO: Implement method
        return false;
    }
}
