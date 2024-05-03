package ui;

import model.*;
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

// A console based UI that generates the dialog, and process user inputs.
public class ShelfLifeTracker {

    private Inventory inventory;
    private Scanner input;
    private static final String JSON_STORE = "./data/Inventory.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the Shelf Life tracker application
    public ShelfLifeTracker() {
        runTracker();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runTracker() {
        boolean keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.nextLine();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
                System.out.println("Thank you for using Food Queue! \n See you next time!");
            } else {
                processCommand(command);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes storage spaces;
    public void init() {
        inventory = new Inventory();
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nWelcome to Food Queue");
        System.out.println("\nPlease Select from the following options:");
        System.out.println("\tc -> [C]hange food Quantity.");
        System.out.println("\td -> [D]elete food item");
        System.out.println("\tl -> [L]oad previous inventory");
        System.out.println("\tn -> Enter [N]ew food item");
        System.out.println("\to -> [O]pen bag (Set shelf life to 3 days).");
        System.out.println("\tp -> [P]rint current inventory");
        System.out.println("\ts -> [S]ave Current Inventory");
        System.out.println("\tt -> [T]ransfer food to another storage.");
        System.out.println("\tq -> [Q]uit");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("c")) {
            doChangeQuantity();
        } else if (command.equals("d")) {
            doDeleteFood();
        } else if (command.equals("l")) {
            doLoadInventory();
        } else if (command.equals("n")) {
            doEnterFood();
        } else if (command.equals("o")) {
            doOpenBag();
        } else if (command.equals("p")) {
            doPrintInventory();
        } else if (command.equals("s")) {
            doSaveInventory(); //TODO: update to save inventory
        } else if (command.equals("t")) {
            doTransfer();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // Modifies: food.
    // Effect: update the quantity.
    public void doChangeQuantity() {
        InventoryPath path = identityToPath();
        doPrintInventory();
        System.out.println("Please enter the new quantity: (e.g 0.25/ 30)");
        String command = input.nextLine();
        double quantity = Double.parseDouble(command);
        path.getFood().setQuantity(quantity);
        System.out.printf("Item" + path.getFood().getName()
                + "(ID:)" + path.getFood().getID()
                + ") now has quantity of" + path.getFood().getQuantity() + " " + path.getFood().getUnit());
    }


    // Modifies: this.
    // Effects: delete food from storage.
    public void doDeleteFood() {
        InventoryPath path = identityToPath();
        doPrintInventory();
        path.getStorage().getStorage().remove(path.getFood());
        System.out.printf("Item" + path.getFood().getName()
                + "(ID:)" + path.getFood().getID()
                + ") is removed from" + path.getStorage().getClass());
    }

    // Modifies: this.
    // Effects: create food item.
    public void doEnterFood() {
        System.out.println("Please enter the food name:");
        String name = input.nextLine();

        Date purchaseDate = enterDate();

        double quantity = enterQuantity();

        System.out.println("Please enter unit:");
        String unit = input.nextLine();

        String type = setType();

        String status = "unopened";

        Storage storageCondition = chooseStorage();

        this.inventory.makeFood(name, purchaseDate, quantity, unit, type, storageCondition);
    }

    // Modifies: this.
    // Effects: create food item.
    public void doOpenBag() {
        doPrintInventory();
        InventoryPath path = identityToPath();
        Food food = path.getFood();
        food.openBag();
    }


    // Effects: print out the entire inventory.
    public void doPrintInventory() {
        ArrayList<Food> allInventory = this.inventory.buildAllInventory();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        System.out.println("Name || ID || Purchase Date "
                + "|| Expiration Date ||  Quantity || Unit || Location || Days Left");
        for (Food food : allInventory) {
            System.out.println(food.getName() + "||" + food.getID() + "||" + sdf.format(food.getPurchaseDate())
                    + "||" + sdf.format(food.getExpirationDate()) + "||" + food.getQuantity() + "||"
                    + food.getUnit() + "||" + food.getStorageConditions().get(food.getStorageConditions().size() - 1)
                    + "||" + food.calculateDaysLeft());
        }
    }

    // Modifies: this.
    // Effects: transfer food between storages.
    public void doTransfer() {
        doPrintInventory();
        InventoryPath path = identityToPath();
        Storage destination = chooseStorage();
        this.inventory.transferStorage(path.getFood(), path.getStorage(), destination);
    }

    // Requires: food in the list, id int and in the list.
    // Effects: use id to select food and its location.
    public InventoryPath identityToPath() {
        System.out.println("please enter the id to select item:");
        String inputId = input.nextLine();
        int identity = Integer.parseInt(inputId);
        InventoryPath path = selectFood(identity);
        return path;
    }

    // Requires: type is one of 1, 2.
    // Effects: use int to set food type.
    public String setType() {
        System.out.println("Is this item perishable [1] (normal food)"
                + "\n non-perishable [2] (canned, low pH, low water activity)?");
        String perishableIndicator = input.nextLine();
        System.out.println("input is" + perishableIndicator);
        if (perishableIndicator.equals("1")) {
            return "perishable";
        } else if (perishableIndicator.equals("2")) {
            return "non-perishable";
        } else {
            System.out.println("Invalid entry, please try again.");
            return setType();
        }
    }

    // Requires: type is one of 1, 2, 3.
    // Effects: use int to select storage.
    public Storage chooseStorage() {
        System.out.println("Please choose the destination of this transaction:");
        System.out.println("\n [1] Pantry;");
        System.out.println("\n [2] Fridge;");
        System.out.println("\n [3] Freezer;");
        String storageIndicator = input.nextLine();
        Storage storageCondition = null;
        if (storageIndicator.equals("1")) {
            storageCondition = this.inventory.getPantry();
        } else if (storageIndicator.equals("2")) {
            storageCondition = this.inventory.getFridge();
        } else if (storageIndicator.equals("3")) {
            storageCondition = this.inventory.getFreezer();
        }
        return storageCondition;
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
        for (Food thisFood :this.inventory.getFreezer().getStorage()) {
            if (thisFood.getID() == identification) {
                return new InventoryPath(this.inventory.getFreezer(), thisFood);
            }
        }

        System.out.printf(identification + "Does not exist in this inventory");
        return null;
    }

    // Requires: inventory non-empty.
    // EFFECTS: saves the workroom to file
    private void doSaveInventory() {
        try {
            if (inventory.buildAllInventory().size() == 0) {
                throw new EmptyStackException();
            }
            jsonWriter.open();
            jsonWriter.write(inventory);
            jsonWriter.close();
            System.out.println("Inventory saved to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        } catch (EmptyStackException e) {
            System.out.println("Inventory is empty, there is nothing to save!");
        }
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void doLoadInventory() {
        try {
            jsonReader.read(this.inventory);
            System.out.println("Loaded inventory from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    public Date enterDate() {
        Date purchaseDate;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        System.out.println("Please purchase date (dd-MM-yyyy):");
        String inputDate = input.nextLine();
        try {
            purchaseDate = sdf.parse(inputDate);
            return purchaseDate;
        } catch (ParseException e) {
            System.out.println("Invalid input format, please double check your input.");
            enterDate();
        }
        return  null;
    }

    public double enterQuantity() {
        try {
            System.out.println("Please enter the quantity:");
            double quantity = Double.parseDouble(input.nextLine());
            return  quantity;
        } catch (Exception e) {
            enterQuantity();
        }
        return 0;
    }
}
