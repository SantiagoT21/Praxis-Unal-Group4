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

    private int maximumQuality = 50;
    private int minimumQuality = 0;
    private int expiredDay = 0;

    Item[] items;

    public ItemService(ItemRepository itemRepository, Item[] items) {
        this.itemRepository = itemRepository;
        this.items = items;
    }

    public List<Item> updateQuality() throws Exception {
        var itemsList = itemRepository.findAll();
        var items = itemsList.toArray(new Item[itemsList.size()]);

        for (Item item : items) {

            int daysForSellUpdate = item.sellIn;
            boolean isExpired = daysForSellUpdate < expiredDay;

            item = itemUpdated(item,isExpired);

            itemRepository.save(item);
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


    public int increaseQuality(int qualityToUpdate) throws Exception{
        boolean isValidQuality = qualityToUpdate<maximumQuality && qualityToUpdate>minimumQuality;
        try{
            if(isValidQuality){
                qualityToUpdate ++;
            }
            else{
                throw new ArithmeticException("the quality has the maximum value");
            }
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
        return qualityToUpdate;
    }

    public int decreaseQuality(int qualityToUpdate) throws Exception {
        boolean isValidQuality = qualityToUpdate<maximumQuality && qualityToUpdate>minimumQuality;
        try{
            if(isValidQuality){
                qualityToUpdate --;
            }
            else{
                throw new ArithmeticException("the quality has the minimum value");
            }
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
        return qualityToUpdate;
    }

    public int updateTicketExpiredQuality(){
        return minimumQuality;
    }

    public int updateTicketQuality(Item item, boolean isExpired) throws Exception {
        int daysForSellUpdate = item.sellIn;
        int qualityToUpdate = item.quality;

        boolean isLastWeekToSellTicket = daysForSellUpdate < 6;
        boolean isLatestTwoWeeksToSellTicket = daysForSellUpdate< 11;

        qualityToUpdate = increaseQuality(qualityToUpdate);

        if(isLatestTwoWeeksToSellTicket){
            qualityToUpdate = increaseQuality(qualityToUpdate);
        }
        if(isLastWeekToSellTicket){
            qualityToUpdate = increaseQuality(qualityToUpdate);
        }
        if(isExpired){
            qualityToUpdate = updateTicketExpiredQuality();
        }

        return qualityToUpdate;
    }

    public int updateNormalQuality(Item item, boolean isExpired) throws Exception {
        int qualityToUpdate = item.quality;

        qualityToUpdate = decreaseQuality(qualityToUpdate);

        if(isExpired){
            qualityToUpdate = decreaseQuality(qualityToUpdate);
        }
        return qualityToUpdate;
    }

    public int updateAgedQuality(Item item, boolean isExpired) throws Exception {
        int qualityToUpdate = item.quality;

        qualityToUpdate = increaseQuality(qualityToUpdate);

        if(isExpired){
            qualityToUpdate = increaseQuality(qualityToUpdate);
        }
        return qualityToUpdate;
    }

    public int decreaseSellIn(Item item){
        return item.sellIn = item.sellIn - 1;
    }

    public Item itemUpdated(Item item, boolean isExpired) throws Exception {
        switch (item.type){
            case NORMAL -> {
                item.quality = updateNormalQuality(item,isExpired);
                item.sellIn = decreaseSellIn(item);
            }
            case AGED -> {
                item.quality =  updateAgedQuality(item,isExpired);
                item.sellIn = decreaseSellIn(item);
            }
            case TICKETS -> {
                item.quality =  updateTicketQuality(item,isExpired);
                item.sellIn = decreaseSellIn(item);
            }
        }
        return item;
    }

}