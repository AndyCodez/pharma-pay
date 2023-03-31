package com.andrew.pharmapay.services;

import com.andrew.pharmapay.models.Item;
import com.andrew.pharmapay.repositories.ItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item createItem(Item item) {
        return itemRepository.save(item);
    }
}
