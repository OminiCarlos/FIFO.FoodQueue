package model;

import java.util.ArrayList;
import java.util.List;


// a selector that refers to the food item in the corresponding storage space
public class InventoryPath {
    private Storage storage;
    private Food food;

    public InventoryPath(Storage storage, Food food) {
        this.storage = storage;
        this.food = food;
    }

    public Storage getStorage() {
        return this.storage;
    }

    public Food getFood() {
        return this.food;
    }
}
