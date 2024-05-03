package persistence;

import model.Food;
import model.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    Inventory inventory = new Inventory();
    Calendar calendar = Calendar.getInstance();
    Date purchaseDate = new Date();
    Date expDate = new Date();
    ArrayList<String> appleList = new ArrayList<>();
    ArrayList<String> orangeList = new ArrayList<>();
    ArrayList<String> beefList = new ArrayList<>();


    @BeforeEach
    public void runBefore() {
        calendar.set(2024,02,07);
        setToStartOfDay(calendar);
        purchaseDate = calendar.getTime();
        calendar.add(Calendar.DATE, 3);
        expDate = calendar.getTime();
        appleList.add("pantry");
        orangeList.add("pantry");
        orangeList.add("fridge");
        beefList.add("pantry");
        beefList.add("freezer");
    }

    public void setToStartOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            reader.read(inventory);
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        } catch (ParseException e) {
            fail("Should not throw parse Exception.");
        }
    }

    @Test
    void testReaderEmptyInventory() {
        JsonReader reader = new JsonReader("data/EmptyInventory.json");
        try {
            reader.read(inventory);
            assertEquals(0, inventory.buildAllInventory().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        } catch (ParseException e) {
            fail("Should not throw this exception.");
        }
    }

    @Test
    void testReaderNormalInventory() {
        JsonReader reader = new JsonReader("./data/InventoryReadTest.json");
        try {
            reader.read(inventory);
            List<Food> pantry = inventory.getPantry().getStorage();
            List<Food> fridge = inventory.getFridge().getStorage();
            List<Food> freezer = inventory.getFreezer().getStorage();
            assertEquals(1, pantry.size());
            assertEquals(1, fridge.size());
            assertEquals(1, freezer.size());
            checkFood(pantry.get(0),"apple",purchaseDate,expDate,
                    2,"kilo", "perishable", "unopened",appleList, 1);
            checkFood(fridge.get(0),"orange",purchaseDate,expDate,
                    1,"bag", "perishable", "unopened",orangeList, 2);
            checkFood(freezer.get(0),"beef",purchaseDate,expDate,
                    2,"lb", "perishable", "unopened",beefList, 3);
        } catch (IOException e) {
            fail("Couldn't read from file");
        } catch (ParseException e) {
            fail("Should not throw this exception.");
        }
    }

    @Test
    void testReaderParsePurException() {
        JsonReader reader = new JsonReader("./data/InventoryParsePurException.json");
        try {
            reader.read(inventory);
            fail("Should throw parse exception");
        } catch (IOException e) {
            fail("wrong exception");
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testReaderParseExpException() {
        JsonReader reader = new JsonReader("./data/InventoryParseExpException.json");
        try {
            reader.read(inventory);
            fail("Should throw parse exception");
        } catch (IOException e) {
            fail("wrong exception");
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }
}