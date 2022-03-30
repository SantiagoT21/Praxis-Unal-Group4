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

            Item currentItem = items[i];

            currentItem = updateBeforeExpired(currentItem);

            if (!currentItem.type.equals(Item.Type.LEGENDARY)) {
                currentItem.sellIn = items[i].sellIn - 1;
            }

            currentItem = updateAfterExpired(currentItem);

            itemRepository.save(currentItem);
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

    public List<Item> listItems(){
        return itemRepository.findAll();
    }

    public Item findById(int id) {
        return itemRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException(""));
    }


    //-------------------------------------------------------------------------------------------
    public Item degradeQuality(Item item) {
        int currentQuality = item.quality;
        if (currentQuality > 0){
            item.quality = currentQuality - 1;
        }
        return item;
    }

    public Item upgradeQuality(Item item) {
        int currentQuality = item.quality;
        if (currentQuality < 50){
            item.quality = currentQuality + 1;
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
                int daysToSell = item.sellIn;
                item = upgradeQuality(item);
                if(daysToSell < 11){
                    item = upgradeQuality(item);
                }
                if(daysToSell < 6){
                    item = upgradeQuality(item);
                }
                return item;
            }
            else{
                return item;
            }
    }

    public Item updateAfterExpired(Item item) {
        int daysToSell = item.sellIn;
        if (daysToSell < 0){
            if(isNormal(item)){
               return  degradeQuality(item);
            }
            if(isAged(item)){
                return upgradeQuality(item);
            }
            if(isTickets(item)){
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
