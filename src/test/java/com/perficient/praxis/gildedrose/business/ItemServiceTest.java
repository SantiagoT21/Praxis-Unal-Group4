package com.perficient.praxis.gildedrose.business;

import com.perficient.praxis.gildedrose.error.ResourceNotFoundException;
import com.perficient.praxis.gildedrose.model.Item;
import com.perficient.praxis.gildedrose.repository.ItemRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ItemServiceTest {

    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;


    @Test
    public void testGetItemByIdWhenItemWasNotFound(){

        when(itemRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                itemService.findById(0));
    }

    @Test
    public void testGetItemByIdSuccess(){

        var item = new Item(0, "Oreo", 10, 30, Item.Type.NORMAL);
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));

        Item itemFound = itemService.findById(0);
        assertEquals(item, itemFound);
    }

    @Test
    /**
     * GIVEN a valid item in the database
     * WHEN createItem method is called
     * THEN the service should create the item and return this
     */
    public void testCreateItemSuccess(){

        var item = new Item(0, "Oreo", 10, 30, Item.Type.NORMAL);
        when(itemRepository.save(any())).thenReturn(item);

        Item itemCreated = itemService.createItem(item);
        assertEquals(item, itemCreated);
    }


    @Test
    /**
     * GIVEN a valid normal type item in the database
     * WHEN updateQuality method is called
     * THEN the service should update the quality and sellIn values,
     * both will be decreased by 1
     */
    public void testUpdateQualityOfNormalTypeItem(){

        var item = new Item(0, "Oreo", 10, 30, Item.Type.NORMAL);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Oreo", itemsUpdated.get(0).name);
        assertEquals(9, itemsUpdated.get(0).sellIn);
        assertEquals(29, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.NORMAL, itemsUpdated.get(0).type);
        verify(itemRepository,times(1)).save(any());
    }
    @Test
    /**
     * GIVEN a valid tickets type item in the database with sellIn value < 6 and quality <50
     * WHEN updateQuality method is called
     * THEN the service should update the quality and sellIn values,
     * the quality will be increased by 3, and the sellin will decreased by 1
     */
    public void testUpdateQualityOfTicketsTypeItemSellInUnder6(){

        var item = new Item(0, "Jumbo Concierto", 5, 45, Item.Type.TICKETS);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Jumbo Concierto", itemsUpdated.get(0).name);
        assertEquals(4, itemsUpdated.get(0).sellIn);
        assertEquals(48, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.TICKETS, itemsUpdated.get(0).type);
        verify(itemRepository,times(1)).save(any());
    }

    @Test
    /**
     * GIVEN a valid tickets type item in the database with sellIn value > 11 and quality <50
     * WHEN updateQuality method is called
     * THEN the service should update the sellIn values,
     * the sellIn will decrease by 1
     */
    public void testUpdateQualityOfTicketsTypeItemSellInOver11(){

        var item = new Item(0, "Jumbo Concierto", 15, 70, Item.Type.TICKETS);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Jumbo Concierto", itemsUpdated.get(0).name);
        assertEquals(14, itemsUpdated.get(0).sellIn);
        assertEquals(70, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.TICKETS, itemsUpdated.get(0).type);
        verify(itemRepository,times(1)).save(any());
    }

    @Test
    /**
     * GIVEN a valid tickets type item in the database with Negative sellIn and positive quality value
     * WHEN updateQuality method is called
     * THEN the service should update the sellIn values,
     * the sellIn will decrease by 1
     */
    public void testUpdateQualityOfTicketsTypeItemSellInUnder0(){

        var item = new Item(0, "Jumbo Concierto", -1, 45, Item.Type.TICKETS);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Jumbo Concierto", itemsUpdated.get(0).name);
        assertEquals(-2, itemsUpdated.get(0).sellIn);
        assertEquals(0, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.TICKETS, itemsUpdated.get(0).type);
        verify(itemRepository,times(1)).save(any());
    }

    @Test
    /**
     * GIVEN a valid Normal type item in the database with Negative sellIn and positive quality value
     * WHEN updateQuality method is called
     * THEN the service should update the sellIn and quality values,
     * the sellIn will decrease by 1, and the quality will decrease by 2 (double)
     */
    public void testUpdateQualityOfNormalTypeItemSellInUnder0(){

        var item = new Item(0, "Oreo", -1, 45, Item.Type.NORMAL);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Oreo", itemsUpdated.get(0).name);
        assertEquals(-2, itemsUpdated.get(0).sellIn);
        assertEquals(43, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.NORMAL, itemsUpdated.get(0).type);
        verify(itemRepository,times(1)).save(any());
    }

    @Test
    /**
     * GIVEN a valid Aged type item in the database with Negative sellIn and quality under 50
     * WHEN updateQuality method is called
     * THEN the service should update the sellIn and quality values,
     * the sellIn will decrease by 1, and the quality will increase by 2
     */
    public void testUpdateQualityOfAgedTypeItemSellInUnder0QualityUnder50(){

        var item = new Item(0, "Wine Black Cat", 0, 45, Item.Type.AGED);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Wine Black Cat", itemsUpdated.get(0).name);
        assertEquals(-1, itemsUpdated.get(0).sellIn);
        assertEquals(47, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.AGED, itemsUpdated.get(0).type);
        verify(itemRepository,times(1)).save(any());
    }

    @Test
    /**
     * GIVEN a valid Aged type item in the database with Negative sellIn and quality over 50
     * WHEN updateQuality method is called
     * THEN the service should update the sellIn and quality values,
     * the sellIn will decrease by 1
     */
    public void testUpdateQualityOfAgedTypeItemSellInUnder0QualityOver50(){

        var item = new Item(0, "Wine Black Cat", 0, 65, Item.Type.AGED);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Wine Black Cat", itemsUpdated.get(0).name);
        assertEquals(-1, itemsUpdated.get(0).sellIn);
        assertEquals(65, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.AGED, itemsUpdated.get(0).type);
        verify(itemRepository,times(1)).save(any());
    }

    @Test
    /**
     * GIVEN a valid Legendary type item in the database with quality 0
     * WHEN updateQuality method is called
     * THEN the service shouldn't update the sellIn and quality values
     */
    public void testUpdateQualityOfLegendaryTypeItemQuality0(){

        var item = new Item(0, "Gioconda", 11, 0, Item.Type.LEGENDARY);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Gioconda", itemsUpdated.get(0).name);
        assertEquals(11, itemsUpdated.get(0).sellIn);
        assertEquals(0, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.LEGENDARY, itemsUpdated.get(0).type);
        verify(itemRepository,times(1)).save(any());
    }

    @Test
    /**
     * GIVEN a valid Legendary type item in the database
     * WHEN updateQuality method is called
     * THEN the service shouldn't update the sellIn and quality values
     */
    public void testUpdateQualityOfLegendaryTypeItem(){

        var item = new Item(0, "Gioconda", 11, 50, Item.Type.LEGENDARY);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Gioconda", itemsUpdated.get(0).name);
        assertEquals(11, itemsUpdated.get(0).sellIn);
        assertEquals(50, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.LEGENDARY, itemsUpdated.get(0).type);
        verify(itemRepository,times(1)).save(any());
    }

    @Test
    /**
     * GIVEN a valid tickets type item in the database with sellIn over 11 and positive quality under 50
     * WHEN updateQuality method is called
     * THEN the service should update the sellIn values,
     * the sellIn will decrease by 1, the quality will increase by 1
     */
    public void testUpdateQualityOfTicketsTypeItemSellInOver11QualityUnder50(){

        var item = new Item(0, "Jumbo Concierto", 12, 45, Item.Type.TICKETS);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Jumbo Concierto", itemsUpdated.get(0).name);
        assertEquals(11, itemsUpdated.get(0).sellIn);
        assertEquals(46, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.TICKETS, itemsUpdated.get(0).type);
        verify(itemRepository,times(1)).save(any());
    }

    @Test
    /**
     * GIVEN a valid tickets type item in the database with sellIn under 6 and quality 49
     * WHEN updateQuality method is called
     * THEN the service should update the sellIn values,
     * the sellIn will decrease by 1, the quality will increase by 1
     */
    public void testUpdateQualityOfTicketsTypeItemSellInUnder6QualityOver50(){

        var item = new Item(0, "Jumbo Concierto", 5, 49, Item.Type.TICKETS);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Jumbo Concierto", itemsUpdated.get(0).name);
        assertEquals(4, itemsUpdated.get(0).sellIn);
        assertEquals(50, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.TICKETS, itemsUpdated.get(0).type);
        verify(itemRepository,times(1)).save(any());
    }

    @Test
    /**
     * GIVEN a valid Legendary type item in the database with sellIn under 0 and quality 0
     * WHEN updateQuality method is called
     * THEN the service shouldn't update the sellIn and quality values
     */
    public void testUpdateQualityOfLegendaryTypeItemSellInUnder0Quality0(){

        var item = new Item(0, "Gioconda", -1, 0, Item.Type.LEGENDARY);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Gioconda", itemsUpdated.get(0).name);
        assertEquals(-1, itemsUpdated.get(0).sellIn);
        assertEquals(0, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.LEGENDARY, itemsUpdated.get(0).type);
        verify(itemRepository,times(1)).save(any());
    }

    @Test
    /**
     * GIVEN a valid Legendary type item in the database with sellIn under 0 and quality over 0
     * WHEN updateQuality method is called
     * THEN the service shouldn't update the sellIn and quality values
     */
    public void testUpdateQualityOfLegendaryTypeItemSellInUnder0QualityOver0(){

        var item = new Item(0, "Gioconda", -1, 10, Item.Type.LEGENDARY);
        when(itemRepository.findAll()).thenReturn(List.of(item));

        List<Item> itemsUpdated = itemService.updateQuality();

        assertEquals(0, itemsUpdated.get(0).getId());
        assertEquals("Gioconda", itemsUpdated.get(0).name);
        assertEquals(-1, itemsUpdated.get(0).sellIn);
        assertEquals(10, itemsUpdated.get(0).quality);
        assertEquals(Item.Type.LEGENDARY, itemsUpdated.get(0).type);
        verify(itemRepository,times(1)).save(any());
    }

}
