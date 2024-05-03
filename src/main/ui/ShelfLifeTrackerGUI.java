package ui;

import model.Food;
import model.Inventory;
import model.InventoryPath;
import model.Storage;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.Scanner;

// the model of shelf life tracker GUI that process all commands.
public class ShelfLifeTrackerGUI {

    private Inventory inventory;
//    private Scanner input;
    private static final String JSON_STORE = "./data/Inventory.json";
    ArrayList<Food> allInventory;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;


    // MODIFIES: this
    // EFFECTS: initializes storage spaces;
    public ShelfLifeTrackerGUI() {
        inventory = new Inventory();
//        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        allInventory = new ArrayList<>();
    }

    // put the inventory items info into a matrix.
    public Object[][] getData() {
        allInventory = inventory.buildAllInventory();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Object[][] inventory = new Object[allInventory.size()][6];
        for (int i = 0; i < allInventory.size(); i++) {
            Food food = allInventory.get(i);
            inventory[i][0] = food.getName();
            inventory[i][1] = sdf.format(food.getPurchaseDate());
            inventory[i][2] = sdf.format(food.getExpirationDate());
            inventory[i][3] = food.getQuantity();
            inventory[i][4] = food.getUnit();
            inventory[i][5] = food.getStorageConditions().get(food.getStorageConditions().size() - 1);
        }
        return inventory;
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    public void doLoadInventory() {
        try {
            jsonReader.read(this.inventory);
//            System.out.println("Loaded inventory from " + JSON_STORE);
        } catch (IOException e) {
//            System.out.println("Unable to read from file: " + JSON_STORE);
        } catch (ParseException e) {
//            System.out.println(e.getMessage());
        }
        allInventory = inventory.buildAllInventory();
    }

    // Requires: inventory non-empty.
    // EFFECTS: saves the workroom to file
    public void doSaveInventory() {
        try {
            if (inventory.buildAllInventory().size() == 0) {
                throw new EmptyStackException();
            }
            jsonWriter.open();
            jsonWriter.write(inventory);
            jsonWriter.close();
//            System.out.println("Inventory saved to " + JSON_STORE);
        } catch (FileNotFoundException e) {
//            System.out.println("Unable to write to file: " + JSON_STORE);
        } catch (EmptyStackException e) {
//            System.out.println("Inventory is empty, there is nothing to save!");
        }
    }

    // Modifies: this.
    // Effects: transfer food between storages.
    public void doTransfer(int index, String receiver) {
        int identity = allInventory.get(index).getID();
        InventoryPath path = identityToPath(identity);
        Storage destination = chooseStorage(receiver);
        this.inventory.transferStorage(path.getFood(), path.getStorage(), destination);
        allInventory = inventory.buildAllInventory();
    }

    // Modifies: this.
    // Effects: create food item.
    public void doEnterFood(String name, String purchaseText, String quantityText, String unit, int typeIndex,
                            int receiverIndex) {

        Date purchaseDate =  convertDate(purchaseText);

        double quantity = Double.parseDouble(quantityText);

        String type = new String();
        if (typeIndex == 0) {
            type = "perishable";
        } else if (typeIndex == 1) {
            type = "non-perishable";
        }

        String receiver = new String();
        if (receiverIndex == 0) {
            receiver = "Pantry";
        } else if (receiverIndex == 1) {
            receiver = "Fridge";
        } else if (receiverIndex == 2) {
            receiver = "Freezer";
        }

        Storage storageCondition = chooseStorage(receiver);
        this.inventory.makeFood(name, purchaseDate, quantity, unit, type, storageCondition);
    }

    // Modifies: this.
    // Effects: delete food from storage.
    public void doDeleteFood(int index) {
        int identity = allInventory.get(index).getID();
        InventoryPath path = identityToPath(identity);
        path.getStorage().removeItem(path.getFood());
//        System.out.printf("Item" + path.getFood().getName()
//                + "(ID:)" + path.getFood().getID()
//                + ") is removed from" + path.getStorage().getClass());
    }


    // Requires: the input should be in "YYYY-MM-dd" format
    // Modifies: convert a date string to a java date value.
    public Date convertDate(String inputDate) {
        Date purchaseDate;
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        try {
            purchaseDate = sdf.parse(inputDate);
            return purchaseDate;
        } catch (ParseException e) {
//            System.out.println("Invalid input format, please double check your input.");
        }
        return null;
    }

    // Requires: food in the list, id int and in the list.
    // Effects: use id to select food and its location.
    public InventoryPath identityToPath(int identity) {
        InventoryPath path = selectFood(identity);
        return path;
    }

    // Requires inventory fields properly set up.
    // Effects: search for food in inventory.
    public InventoryPath selectFood(int identification) {

        for (Food thisFood : this.inventory.getPantry().getStorage()) {
            if (thisFood.getID() == identification) {
                return new InventoryPath(this.inventory.getPantry(), thisFood);
            }
        }
        for (Food thisFood : this.inventory.getFridge().getStorage()) {
            if (thisFood.getID() == identification) {
                return new InventoryPath(this.inventory.getFridge(), thisFood);
            }
        }
        for (Food thisFood : this.inventory.getFreezer().getStorage()) {
            if (thisFood.getID() == identification) {
                return new InventoryPath(this.inventory.getFreezer(), thisFood);
            }
        }

        System.out.printf(identification + "Does not exist in this inventory");
        return null;
    }


    // Requires: type is one of "Pantry", "Fridge", "Freezer".
    // Effects: use String to select storage.
    public Storage chooseStorage(String receiver) {
        String storageIndicator = receiver;
        Storage storageCondition = null;
        if (storageIndicator.equals("Pantry")) {
            storageCondition = this.inventory.getPantry();
        } else if (storageIndicator.equals("Fridge")) {
            storageCondition = this.inventory.getFridge();
        } else if (storageIndicator.equals("Freezer")) {
            storageCondition = this.inventory.getFreezer();
        }
        return storageCondition;
    }

}