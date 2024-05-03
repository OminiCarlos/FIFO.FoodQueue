package model;

import java.util.ArrayList;

// Storage condition, like freezer, inventory, normal temperature
public abstract class Storage {

    protected ArrayList<Food> inventory;
    protected String storageType;

    protected Storage() {
        inventory = new ArrayList<Food>();
    }

    public void addToStorage(Food food) {
        inventory.add(food);
    }

    public ArrayList<Food> getStorage() {
        return inventory;
    }

    public void removeItem(Food food) {
        inventory.remove(food);
        EventLog.getInstance().logEvent(new Event("Item " + food.getName() + " is removed from" + storageType));
    }

    public String getStorageType() {
        return storageType;
    }

}
