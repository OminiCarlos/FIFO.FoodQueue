package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class FoodTest {
    public Inventory inventory;
    public Food cacao;
    public Food cabbage;
    public Food shrimp;
    public Food rice;
    public Food soySauce;
    public Food cannedFish;
    public Calendar calendar = Calendar.getInstance();
    public Date lastChristmas;
    public Date thisNewYear;

    @BeforeEach
    public void runBefore() {
        inventory = new Inventory();
        cacao = new Food("Cacao", new Date(), 12, "bag", inventory);
        cacao.setType("perishable");
        cacao.setExpirationDate(new Date());
        inventory.dispatchFood(cacao, inventory.getPantry());
        cabbage = new Food("cabbage", new Date(), 12, "each", inventory);
        cabbage.setType("perishable");
        inventory.dispatchFood(cabbage, inventory.getFridge());
        shrimp = new Food("shrimp", new Date(), 12, "bag", inventory);
        shrimp.setType("perishable");
        inventory.dispatchFood(shrimp, inventory.getFreezer());
        rice = new Food("rice", new Date(), 12, "kg", inventory);
        rice.setType("non-perishable");
        inventory.dispatchFood(rice, inventory.getPantry());
        soySauce = new Food("soySauce", new Date(), 12, "bottle", inventory);
        soySauce.setType("non-perishable");
        inventory.dispatchFood(soySauce, inventory.getFridge());
        cannedFish = new Food("cannedFish", new Date(), 12, "can", inventory);
        cannedFish.setType("non-perishable");
        inventory.dispatchFood(cannedFish, inventory.getFreezer());

        calendar.set(2023, 11, 24, 15, 00);
        lastChristmas = calendar.getTime();
        calendar.set(2024, 00, 01, 00, 00);
        thisNewYear = calendar.getTime();
    }

//    @Test
//    public void testDefaultExp() {
//        assertEquals(0, cacao.getIndexTable().get("perishable"));
//        assertEquals(1, cacao.getIndexTable().get("non-perishable"));
//        assertEquals(0, cacao.getIndexTable().get("pantry"));
//        assertEquals(1, cacao.getIndexTable().get("fridge"));
//        assertEquals(2, cacao.getIndexTable().get("freezer"));
//    }

//    @Test
//    public void TestInitializeExpTable() {
//        assertEquals(3, cacao.getMatrixRep().get(cacao.getIndexTable().get("perishable"))
//                .get(cacao.getIndexTable().get("pantry")));
//        assertEquals(90, cacao.getMatrixRep().get(cacao.getIndexTable().get("non-perishable"))
//                .get(cacao.getIndexTable().get("pantry")));
//        assertEquals(5, cacao.getMatrixRep().get(cacao.getIndexTable().get("perishable"))
//                .get(cacao.getIndexTable().get("fridge")));
//        assertEquals(90, cacao.getMatrixRep().get(cacao.getIndexTable().get("non-perishable"))
//                .get(cacao.getIndexTable().get("fridge")));
//        assertEquals(30, cacao.getMatrixRep().get(cacao.getIndexTable().get("perishable"))
//                .get(cacao.getIndexTable().get("freezer")));
//        assertEquals(90, cacao.getMatrixRep().get(cacao.getIndexTable().get("non-perishable"))
//                .get(cacao.getIndexTable().get("freezer")));
//    }

    @Test
    public void testCalculateDaysLeft() {
        calendar = Calendar.getInstance();
        setToStartOfDay(calendar);
        calendar.add(Calendar.DATE, 3);
        cacao.setExpirationDate(calendar.getTime());
        assertEquals(3, cacao.calculateDaysLeft());
    }

    @Test
    public void testCalculateDifferenceInDays() {
        assertEquals(7, cacao.calculateDifferenceInDays(lastChristmas, thisNewYear));
        calendar.setTime(lastChristmas);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date lastChristmasEve = calendar.getTime();
        assertEquals(8, cacao.calculateDifferenceInDays(lastChristmasEve, thisNewYear));
    }

    @Test
    void testCalculateShelfLife() {
        String storageCondition = cacao.getStorageConditions().get(0);
        cacao.calculateShelfLife(storageCondition);
    }

    @Test
    public void testInitializeExpirationDatePerishablePantry() {
        calendar.set(2024, 00, 31);
        Date purchaseDate = calendar.getTime();
        calendar.set(2024, 01, 03);
        Date expectedExp = calendar.getTime();
        cacao.setPurchaseDate(purchaseDate);
        cacao.initializeExpirationDate();
        assertEquals(expectedExp, cacao.getExpirationDate());
    }

    @Test
    public void testInitializeExpirationDatePerishableFridge() {
        calendar.set(2024, 00, 31);
        Date purchaseDate = calendar.getTime();
        calendar.set(2024, 02, 01);
        Date expectedExp = calendar.getTime();
        shrimp.setPurchaseDate(purchaseDate);
        shrimp.initializeExpirationDate();
        assertEquals(expectedExp, shrimp.getExpirationDate());
    }

    @Test
    public void testInitializeExpirationDatePerishableFreezer() {
        calendar.set(2024, 00, 31);
        Date purchaseDate = calendar.getTime();
        calendar.set(2024, 01, 05);
        Date expectedExp = calendar.getTime();
        cabbage.setPurchaseDate(purchaseDate);
        cabbage.initializeExpirationDate();
        assertEquals(expectedExp, cabbage.getExpirationDate());
    }

    @Test
    public void testInitializeExpirationDateNonPerishablePantry() {
        calendar.set(2024, 00, 31);
        Date purchaseDate = calendar.getTime();
        calendar.set(2024, 03, 30);
        Date expectedExp = calendar.getTime();
        rice.setPurchaseDate(purchaseDate);
        rice.initializeExpirationDate();
        assertEquals(expectedExp, rice.getExpirationDate());
    }

    @Test
    public void testInitializeExpirationDateNonPerishableFridge() {
        calendar.set(2024, 00, 31);
        Date purchaseDate = calendar.getTime();
        calendar.set(2024, 03, 30);
        Date expectedExp = calendar.getTime();
        soySauce.setPurchaseDate(purchaseDate);
        soySauce.initializeExpirationDate();
        assertEquals(expectedExp, soySauce.getExpirationDate());
    }

    @Test
    public void testInitializeExpirationDateNonPerishableFreezer() {
        calendar.set(2024, 00, 31);
        Date purchaseDate = calendar.getTime();
        calendar.set(2024, 03, 30);
        Date expectedExp = calendar.getTime();
        cannedFish.setPurchaseDate(purchaseDate);
        cannedFish.initializeExpirationDate();
        assertEquals(expectedExp, cannedFish.getExpirationDate());
    }


    @Test
    public void testUpdateExpDatePerishablePantry() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        calendar.set(2024, 02, 05); // 05-Mar-2024
        setToStartOfDay(calendar);
        Date purchaseDate = calendar.getTime();
        cacao.setPurchaseDate(purchaseDate); // 05-Mar-2024
        calendar.set(2024, 02,8);
        Date expectedExp = calendar.getTime();
        cacao.initializeExpirationDate();
        cacao.updateExpDate(inventory.getFridge());
        String expDate = sdf.format(cacao.getExpirationDate());
        Date actualDate = cacao.getExpirationDate();;
        assertEquals(expectedExp.getTime(), cacao.getExpirationDate().getTime());
    }

    @Test
    public void testOpenBag() {
        calendar.set(2024, 01, 18, 00, 00, 00);
        calendar.set(Calendar.MILLISECOND, 0);
        Date purchaseDate = calendar.getTime();
        cacao.setPurchaseDate(purchaseDate);
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,+3);
        setToStartOfDay(calendar);
        Date expectedExp = calendar.getTime();
        cacao.openBag();
        assertEquals(expectedExp, cacao.getExpirationDate());
    }

    @Test
    public void testAssignID() {
        Food beef = new Food("beef", new Date(), 2, "cuts", inventory);
        beef.assignID(inventory);
        assertEquals(7, beef.getID());
    }

    @Test
    public void testSetName() {
        cacao.setName("Cacao 70");
        assertEquals("Cacao 70", cacao.getName());
    }

    @Test
    public void testSetQuantity() {
        cacao.setQuantity(12.5);
        assertEquals(12.5, cacao.getQuantity());
    }

    @Test
    public void testSetUnit() {
        cacao.setUnit("lbs");
        assertEquals("lbs", cacao.getUnit());
    }

    @Test
    public void testSetID() {
        cacao.setID(18);
        assertEquals(18, cacao.getID());
    }

    @Test
    public void testSetType() {
        cacao.setType("perishable");
        assertEquals("perishable", cacao.getType());
    }

    @Test
    public void testSetStatus() {
        cacao.setStatus("unopened");
        assertEquals("unopened", cacao.getStatus());
    }

    @Test
    public void testGetPurchaseDate() {
        Date date = new Date();
        cacao.setPurchaseDate(date);
        assertEquals(date, cacao.getPurchaseDate());
    }

    @Test
    public void testSetStorageConditions() {
        ArrayList<String> newConditions = new ArrayList<>();
        newConditions.add("pantry");
        newConditions.add("shelf");
        cacao.setStorageConditions(newConditions);
        assertEquals("pantry",cacao.getStorageConditions().get(0));
        assertEquals("shelf",cacao.getStorageConditions().get(1));
    }

    @Test
    public void testToJSON(){
        JSONObject cacaoJSON = new JSONObject();
        cacaoJSON = cacao.toJson();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        JSONObject refCacao = new JSONObject();

        refCacao.put("name", cacao.getName());
        refCacao.put("purchaseDate", sdf.format(cacao.getPurchaseDate()));
        refCacao.put("expirationDate", sdf.format(cacao.getExpirationDate()));
        refCacao.put("quantity", cacao.getQuantity());
        refCacao.put("unit", cacao.getUnit());
        refCacao.put("type", cacao.getType());
        refCacao.put("status", cacao.getStatus());
        refCacao.put("storageConditions", new JSONArray(cacao.getStorageConditions()));
        refCacao.put("id", cacao.getID());

        assertEquals(cacaoJSON.get("name"),refCacao.get("name"));
    }

    public void setToStartOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }


}

