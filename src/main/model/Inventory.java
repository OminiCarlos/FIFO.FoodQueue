package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Date;

// keeps track of where the food is stored.
public class Inventory implements Writable {

    private Fridge fridge;
    private Freezer freezer;
    private Pantry pantry;

    // MODIFIES: this
    // EFFECTS: initializes storage spaces;
    public Inventory() {
        pantry = new Pantry();
        fridge = new Fridge();
        freezer = new Freezer();
    }

    // Effects: get Freezer in the inventory.
    public Freezer getFreezer() {
        return freezer;
    }

    // Effects: get pantry in the inventory.
    public Pantry getPantry() {
        return pantry;
    }

    // Effects: get fridge in the inventory.
    public Fridge getFridge() {
        return fridge;
    }

    // Requires: inventory fields exist.
    // Effect: merges 3 storage spaces into a master list.
    public ArrayList<Food> buildAllInventory() {
        ArrayList<Food> allInventory = new ArrayList<Food>();
        allInventory.addAll(pantry.getStorage());
        allInventory.addAll(fridge.getStorage());
        allInventory.addAll(freezer.getStorage());
        return allInventory;
    }

    // Requires: Input dates are valid, purchaseDate < expDate.
    // Modifies: this and food.
    // Effect: create a food, specify all fields, assign it to a storage;
    public Food makeFood(String name, Date purchaseDate, double quantity, String unit, String type,
                         Storage storageCondition) {
        Food food = new Food(name, purchaseDate, quantity, unit, this);
        food.setType(type);
        food.setStatus("unopened");
        this.dispatchFood(food, storageCondition);
        food.initializeExpirationDate();
        EventLog.getInstance().logEvent(new Event(name + " is saved to " + storageCondition.getStorageType()));
        return food;
    }

    // Requires: destination is a field in this inventory;
    // Modifies: this.
    // Effects: dispatches food to specified list, one of "pantry", "fridge", "freezer";
    public void dispatchFood(Food food, Storage destination) {
        food.getStorageConditions().add(destination.getStorageType());
        destination.addToStorage(food);
    }

    // Requires: food in the storage.
    // Modifies: food, this.
    // Effect: transfer item from fridge to freezer
    public void transferStorage(Food food, Storage from, Storage to) {

        String fromString = from.getStorageType();
        String toString = to.getStorageType();
        String direction = fromString + "->" + toString;


//        if (from == to) {
//            System.out.println("This item is already there. \n Transfer denied.");
//        } else if (to == this.getFreezer() && food.getStorageConditions().contains("freezer")) {
//            System.out.println("Do not refreeze!!!\n This transfer is denied.");
//        } else {
        from.getStorage().remove(food);
        food.getStorageConditions().add(toString);
        food.updateExpDate(to);
        to.addToStorage(food);
        EventLog.getInstance().logEvent(new Event(food.getName() + " is transferred " + direction));
//            System.out.println(food.getName() + " Transfer succeeded." + direction);
//        }
    }

    // Effects: convert the inventory to JSON
    @Override
    public JSONObject toJson() {

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        ArrayList<Food> allInventory = buildAllInventory();

        for (Food item : allInventory) {
            jsonArray.put(item.toJson());
        }

        jsonObject.put("inventory", jsonArray);
        return jsonObject;
    }
}
