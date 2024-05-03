package persistence;

import model.Food;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    protected void checkFood(
            Food food, String name, Date purchaseDate, Date expirationDate,
            double quantity, String unit, String type, String status,
            ArrayList<String> storageConditions, int id) {

        assertEquals(name, food.getName());
        assertEquals(purchaseDate, food.getPurchaseDate());
        assertEquals(expirationDate, food.getExpirationDate());
        assertEquals(quantity, food.getQuantity());
        assertEquals(unit, food.getUnit());
        assertEquals(type, food.getType());
        assertEquals(status, food.getStatus());
        assertEquals(storageConditions, food.getStorageConditions());
        assertEquals(id, food.getID());
    }
}
