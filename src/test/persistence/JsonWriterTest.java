package persistence;

import model.Food;
import model.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    Inventory inventory = new Inventory();
    Calendar calendar = Calendar.getInstance();
    Date purchaseDate = new Date();
    Date expDate = new Date();
    ArrayList<String> appleList = new ArrayList<>();
    ArrayList<String> orangeList = new ArrayList<>();
    ArrayList<String> beefList = new ArrayList<>();

    @BeforeEach
    public void runBefore() {
        setToStartOfDay(calendar);
        purchaseDate = calendar.getTime();
        calendar.add(Calendar.DATE, 3);
        expDate = calendar.getTime();
        Food apple = inventory.makeFood("apple", purchaseDate,
                2, "kilo", "perishable", inventory.getPantry());
        Food orange = inventory.makeFood("orange", purchaseDate,
                1, "bag", "perishable", inventory.getPantry());
        Food beef = inventory.makeFood("beef", purchaseDate,
                2, "lb", "perishable", inventory.getPantry());
        inventory.transferStorage(orange,inventory.getPantry(),inventory.getFridge());
        inventory.transferStorage(beef,inventory.getPantry(),inventory.getFreezer());
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
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyInventory() {
        try {
            Inventory emptyInventory =  new Inventory();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyInventory.json");
            writer.open();
            writer.write(emptyInventory);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyInventory.json");
            assertEquals(0, emptyInventory.buildAllInventory().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralInventory() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterInventory.json");
            writer.open();
            writer.write(inventory);
            writer.close();
        } catch (Exception e) {
            System.out.printf("Should not throw exception.");
        }

        Inventory newInventory = new Inventory();
        JsonReader reader = new JsonReader("./data/testWriterInventory.json");
        try {
            reader.read(newInventory);
            List<Food> pantry = newInventory.getPantry().getStorage();
            List<Food> fridge = newInventory.getFridge().getStorage();
            List<Food> freezer = newInventory.getFreezer().getStorage();
            assertEquals(1, pantry.size());
            assertEquals(1, fridge.size());
            assertEquals(1, freezer.size());
            checkFood(pantry.get(0), "apple", purchaseDate, expDate,
                    2, "kilo", "perishable", "unopened", appleList, 1);
            checkFood(fridge.get(0), "orange", purchaseDate, expDate,
                    1, "bag", "perishable", "unopened", orangeList, 2);
            checkFood(freezer.get(0), "beef", purchaseDate, expDate,
                    2, "lb", "perishable", "unopened", beefList, 3);
        } catch (IOException e) {
            fail("Couldn't read from file");
        } catch (ParseException e) {
            fail("should not throw this exception.");
        }
    }
}