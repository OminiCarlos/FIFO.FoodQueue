package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryPathTest {

    Inventory inventory;
    InventoryPath inventoryPath;
    Food cacao;

    @BeforeEach
    public void runBefore() {
        inventory =  new Inventory();
        cacao = inventory.makeFood("cacao", new Date(), 12, "bag", "perishable",
                inventory.getPantry());
        inventoryPath = new InventoryPath(inventory.getPantry(),cacao);
    }

    @Test
    public void testGetStorage() {
        assertEquals(inventory.getPantry(),inventoryPath.getStorage());
    }

    @Test
    public void testGetFood() {
        assertEquals(inventory.getPantry().getStorage().get(0),
                inventoryPath.getFood());
    }

}
