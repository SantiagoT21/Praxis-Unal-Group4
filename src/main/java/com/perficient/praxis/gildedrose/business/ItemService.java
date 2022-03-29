package com.perficient.praxis.gildedrose.business;

import com.perficient.praxis.gildedrose.error.ResourceNotFoundException;
import com.perficient.praxis.gildedrose.model.Item;
import com.perficient.praxis.gildedrose.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    Item[] items;

    public ItemService(ItemRepository itemRepository, Item[] items) {
        this.itemRepository = itemRepository;
        this.items = items;
    }

    public List<Item> updateQuality() {
        var itemsList = itemRepository.findAll();
        var items = itemsList.toArray(new Item[itemsList.size()]);

        for (int i = 0; i < items.length; i++) {

            if (items[i].type.equals(Item.Type.NORMAL) && items[i].quality > 0 && items[i].quality < 50) {

                items[i].quality = items[i].quality - 1;

            }

            if (items[i].type.equals(Item.Type.TICKETS) && items[i].quality < 50) {

                items[i].quality = items[i].quality + 1;

                if (items[i].sellIn < 11 && items[i].quality < 50) {

                    items[i].quality = items[i].quality + 1;
                }
                if (items[i].sellIn < 6 && items[i].quality < 50) {

                    items[i].quality = items[i].quality + 1;
                }
            }

            if (!items[i].type.equals(Item.Type.LEGENDARY)) {

                items[i].sellIn = items[i].sellIn - 1;

            }

            if (items[i].type.equals(Item.Type.AGED) && items[i].sellIn < 0 && items[i].quality < 50) {

                items[i].quality = items[i].quality + 1;

                if (items[i].quality < 50) {
                    items[i].quality = items[i].quality + 1;
                }

            }

            if (items[i].type.equals(Item.Type.NORMAL) && items[i].sellIn < 0 && items[i].quality > 0) {

                items[i].quality = items[i].quality - 1;

            }

            if (items[i].type.equals(Item.Type.TICKETS) && items[i].sellIn < 0) {

                items[i].quality = items[i].quality - items[i].quality;
            }

            itemRepository.save(items[i]);
        }
        return Arrays.asList(items);
    }

    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    public Item updateItem(int id, Item item) {
        findById(id);
        return itemRepository.save(new Item(id, item.name, item.sellIn, item.quality, item.type));
    }

    public List<Item> listItems() {
        return itemRepository.findAll();
    }

    public Item findById(int id) {
        return itemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(""));
    }
}
