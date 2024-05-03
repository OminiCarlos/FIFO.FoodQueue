package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class StorageTest {

    public Storage storage;
    public Food cacao;
    public Food cabbage;
    public Inventory inventory;

    @BeforeEach
    public void runBefore() {
        storage = new Fridge();
        inventory = new Inventory();
        cacao = new Food("Cacao", new Date(), 12, "bag", inventory);
        cacao.setType("perishable");
        cabbage = new Food("cabbage", new Date(), 12, "each", inventory);
        cabbage.setType("perishable");
        ;
    }

    @Test
    public void testAddToStorage() {
        storage.addToStorage(cacao);
        storage.addToStorage(cabbage);
        assertEquals(cacao, storage.getStorage().get(0));
        assertEquals(cabbage, storage.getStorage().get(1));
    }



}
