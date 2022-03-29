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
            if (!items[i].type.equals(Item.Type.AGED)
                    && !items[i].type.equals(Item.Type.TICKETS)) {
                if (items[i].quality > 0) {
                    if (!items[i].type.equals(Item.Type.LEGENDARY)) {
                        items[i].quality = items[i].quality - 1;
                    }
                }
            } else {
                if (items[i].quality < 50) {
                    items[i].quality = items[i].quality + 1;

                    if (items[i].type.equals(Item.Type.TICKETS)) {
                        if (items[i].sellIn < 11) {
                            if (items[i].quality < 50) {
                                items[i].quality = items[i].quality + 1;
                            }
                        }

                        if (items[i].sellIn < 6) {
                            if (items[i].quality < 50) {
                                items[i].quality = items[i].quality + 1;
                            }
                        }
                    }
                }
            }

            if (!items[i].type.equals(Item.Type.LEGENDARY)) {
                items[i].sellIn = items[i].sellIn - 1;
            }

            if (items[i].sellIn < 0) {
                if (!items[i].type.equals(Item.Type.AGED)) {
                    if (!items[i].type.equals(Item.Type.TICKETS)) {
                        if (items[i].quality > 0) {
                            if (!items[i].type.equals(Item.Type.LEGENDARY)) {
                                items[i].quality = items[i].quality - 1;
                            }
                        }
                    } else {
                        items[i].quality = items[i].quality - items[i].quality;
                    }
                } else {
                    if (items[i].quality < 50) {
                        items[i].quality = items[i].quality + 1;
                    }
                }
            }
            itemRepository.save(items[i]);
        }
        return Arrays.asList(items);
    }


    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    public Item updateItem(int id, Item item) {
        return itemRepository.save(new Item(id, item.name, item.sellIn, item.quality, item.type));
    }

    public List<Item> listItems(){
        return itemRepository.findAll();
    }

    public Item findById(int id) {
        return itemRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException(""));
    }


    //-------------------------------------------------------------------------------------------
    public Item degradeQuality(Item item) {
        if (item.quality > 0){
            item.quality = item.quality - 1;
        }
        return item;
    }

    public Item upgradeQuality(Item item) {
        if (item.quality < 50){
            item.quality = item.quality + 1;
        }
        return item;
    }

    public Item updateBeforeExpired(Item item) {
            if(isNormal(item)){
                return  degradeQuality(item);
            }
            if(isAged(item)){
                return upgradeQuality(item);
            }
            if(isTickets(item)){
                //question
                item = upgradeQuality(item);
                if(item.sellIn < 11){
                    item = upgradeQuality(item);
                }
                if(item.sellIn < 6){
                    item = upgradeQuality(item);
                }
            }
            else return item;
    }

    public Item updateAfterExpired(Item item) {
        if (item.sellIn < 0){
            if(isNormal(item)){
               return  degradeQuality(item);
            }
            if(isAged(item)){
                return upgradeQuality(item);
            }
            if(isTickets(item)){
                //question
                item.quality = 0;
                return item;
            }
            else return item;
        }
            return item;
    }

    //-------------------------------------------------------------------------------------------
    public boolean isNormal(Item item) {
        if (item.type.equals(Item.Type.NORMAL)){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isAged(Item item) {
        if (item.type.equals(Item.Type.AGED)){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isTickets(Item item) {
        if (item.type.equals(Item.Type.TICKETS)){
            return true;
        }
        else{
            return false;
        }
    }

}
