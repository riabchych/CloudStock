package com.riabchych.cloudstock.service;

import com.riabchych.cloudstock.entity.Inventory;

import java.util.List;

public interface InventoryService {
    boolean addInventory(Inventory inventory);

    Inventory getInventoryById(long id);

    void updateInventory(Inventory inventory);

    void deleteInventory(Long id);

    List<Inventory> getAllInventorys();
}
