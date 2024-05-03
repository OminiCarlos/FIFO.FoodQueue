package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InventoryTest {

    public Inventory inventory;
    public Food cacao;
    public Food cabbage;
    public Food shrimp;
    public Food rice;
    public Food soySauce;
    public Food cannedFish;

    @BeforeEach
    public void runBefore() {
        inventory = new Inventory();
        cacao = inventory.makeFood("cacao", new Date(), 12, "bag", "perishable",
                inventory.getPantry());
        cabbage = inventory.makeFood("cabbage", new Date(), 12, "each", "perishable",
                inventory.getFridge());
        shrimp = inventory.makeFood("shrimp", new Date(), 12, "bag", "perishable",
                inventory.getFreezer());
        rice = inventory.makeFood("rice", new Date(), 12, "bag", "non-perishable",
                inventory.getPantry());
        soySauce = inventory.makeFood("soy sauce", new Date(), 12, "each", "non-perishable",
                inventory.getFridge());
        cannedFish = inventory.makeFood("canned fish", new Date(), 12, "bag", "non-perishable",
                inventory.getFreezer());
    }

    @Test
    public void testBuildAllInventory() {
        ArrayList<Food> referenceList = new ArrayList<Food>();
        referenceList.add(cacao);
        referenceList.add(rice);
        referenceList.add(cabbage);
        referenceList.add(soySauce);
        referenceList.add(shrimp);
        referenceList.add(cannedFish);
        assertEquals(referenceList, inventory.buildAllInventory());
    }

    @Test
    public void testMakeFood() {
        inventory.makeFood("beef", new Date(), 1, "box", "perishable"
                , inventory.getFridge());
        inventory.makeFood("curry", new Date(), 1, "box", "non-perishable"
                , inventory.getPantry());
        assertEquals(7, inventory.getFridge().getStorage().get(2).getID());
        assertEquals(8, inventory.getPantry().getStorage().get(2).getID());
    }

    @Test
    public void testDispatchFood() {
        inventory.dispatchFood(cacao, inventory.getFridge());
        assertEquals(inventory.getFridge().getStorageType(), cacao.getStorageConditions()
                .get(cacao.getStorageConditions().size() - 1));
        inventory.dispatchFood(cacao, inventory.getFreezer());
        assertEquals(inventory.getFreezer().getStorageType(), cacao.getStorageConditions()
                .get(cacao.getStorageConditions().size() - 1));
        assertEquals(inventory.getFridge().getStorageType(), cacao.getStorageConditions()
                .get(cacao.getStorageConditions().size() - 2));
        assertEquals(inventory.getPantry().getStorageType(), cacao.getStorageConditions()
                .get(cacao.getStorageConditions().size() - 3));
    }

    @Test
    public void testTransferStorage() {
        inventory.transferStorage(cacao, inventory.getPantry(), inventory.getFridge());
        assertEquals(rice, inventory.getPantry().getStorage().get(0));
        assertEquals(cacao, inventory.getFridge().getStorage().get(2));
        assertEquals(cabbage, inventory.getFridge().getStorage().get(0));
        assertEquals(soySauce, inventory.getFridge().getStorage().get(1));

        inventory.transferStorage(cacao, inventory.getFridge(), inventory.getFridge());
        assertEquals(rice, inventory.getPantry().getStorage().get(0));
        assertEquals(cacao, inventory.getFridge().getStorage().get(2));
        assertEquals(cabbage, inventory.getFridge().getStorage().get(0));
        assertEquals(soySauce, inventory.getFridge().getStorage().get(1));

        inventory.transferStorage(cacao, inventory.getFridge(), inventory.getFreezer());
        assertEquals(cabbage, inventory.getFridge().getStorage().get(0));
        assertEquals(soySauce, inventory.getFridge().getStorage().get(1));
        assertEquals(cacao, inventory.getFreezer().getStorage().get(2));

        inventory.transferStorage(cacao, inventory.getFreezer(), inventory.getPantry());
        assertEquals(shrimp, inventory.getFreezer().getStorage().get(0));
        assertEquals(cannedFish, inventory.getFreezer().getStorage().get(1));
        assertEquals(cacao, inventory.getPantry().getStorage().get(1));

        inventory.transferStorage(cacao, inventory.getPantry(), inventory.getFreezer());
        assertEquals(shrimp, inventory.getFreezer().getStorage().get(0));
        assertEquals(cannedFish, inventory.getFreezer().getStorage().get(1));
        assertEquals(cacao, inventory.getPantry().getStorage().get(1));
    }

    @Test
    public void testToJSON(){
        JSONObject inventoryJSON = new JSONObject();
        inventoryJSON = inventory.toJson();
        JSONArray arrayTested = new JSONArray();
        JSONObject foodTested = new JSONObject();

        arrayTested = inventoryJSON.getJSONArray("inventory");
        foodTested = arrayTested.getJSONObject(0);

        assertEquals("cacao",foodTested.get("name"));

    }
}
