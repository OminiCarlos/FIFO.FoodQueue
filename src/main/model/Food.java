package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;

// Food is a food item that contains all the information.
public class Food implements Writable {

    private String name;
    private Date purchaseDate;
    private Date expirationDate;
    private double quantity;
    private String unit;
    private String type;
    private String status;
    private ArrayList<String> storageConditions;
    private int id;
//    private HashMap<String, Integer> indexTable = new HashMap<String, Integer>();
//    private ArrayList<ArrayList<Integer>> matrixRep = new ArrayList<>();

    public Food(String name, Date purchaseDate, double quantity, String unit, Inventory inventory) {
        this.name = name;
        this.purchaseDate = purchaseDate;
        this.quantity = quantity;
        this.unit = unit;
        this.id = assignID(inventory);
//        initializeExpTable();
//        mapTypeAndConditions();
        storageConditions = new ArrayList<String>();
    }

//    // Modifies: this;
//    // Effects: generate a map for the factors that determines the shelf life.
//    public void mapTypeAndConditions() {
//        indexTable.put("perishable", 0);
//        indexTable.put("non-perishable", 1);
//        indexTable.put("pantry", 0);
//        indexTable.put("fridge", 1);
//        indexTable.put("freezer", 2);
//    }

//    // MODIFIES: This
//    // Effects: initialize a map storing the default shelf life.
//    public void initializeExpTable() {
//        ArrayList<Integer> perishable = new ArrayList<>();
//        perishable.add(3); // regular
//        perishable.add(5); // fridge
//        perishable.add(30); // freezer
//
//        ArrayList<Integer> nonPerishable = new ArrayList<>();
//        nonPerishable.add(90); // regular
//        nonPerishable.add(90); // fridge
//        nonPerishable.add(90); // freezer
//
//        matrixRep.add(perishable);
//        matrixRep.add(nonPerishable);
//    }

    // Modifies: this.
    // Effects: set name of the food.
    public void setName(String name) {
        this.name = name;
    }

    // Requires: date is a valid date (feb 39 2024 is invalid).
    // Modifies: this.
    // Effects: set PurchaseDate.
    public void setPurchaseDate(Date date) {
        this.purchaseDate = date;
    }

    // Requires: date is a valid date (feb 39 2024 is invalid).
    // Modifies: this.
    // Effects: set Expiration Date.
    public void setExpirationDate(Date date) {
        this.expirationDate = date;
    }

    // Requires: quantity >= 0
    // Modifies: this.
    // Effects: set Expiration Date.
    public void setQuantity(double num) {
        this.quantity = num;
    }

    // Requires: unit uniform by the food type. rice should always be in kg not bag or kgs,
    // canned fish always in can, not grams or cans.
    // Modifies: this.
    // Effects: set unit.
    public void setUnit(String unit) {
        this.unit = unit;
    }

    // Requires: type: one of perishable/ non-perishable;
    // Modifies: this.
    // Effects: setType.
    public void setType(String type) {
        this.type = type;
    }

    // Requires: type: one of opened/ unopened;
    // Modifies: this.
    // Effects: set Status.
    public void setStatus(String status) {
        this.status = status;
    }

    // Requires: must be of int;
    // Modifies: this.
    // Effects: setID.
    public void setID(int id) {
        this.id = id;
    }

    // Effects: set storage conditions;
    public void setStorageConditions(ArrayList<String> storageConditions) {
        this.storageConditions = storageConditions;
    }

    // Effects: get name;
    public String getName() {
        return this.name;
    }

    // Effects: get Purchase Date;
    public Date getPurchaseDate() {
        return this.purchaseDate;
    }

    // Effects: get Exp Date;
    public Date getExpirationDate() {
        return this.expirationDate;
    }

    // Effects: get quantity;
    public double getQuantity() {
        return this.quantity;
    }

    // Effects: get unit;
    public String getUnit() {
        return this.unit;
    }

    // Effects: get type;
    public String getType() {
        return this.type;
    }

    // Effects: get ID;
    public int getID() {
        return this.id;
    }

    // Effects: get storage conditions;
    public ArrayList<String> getStorageConditions() {
        return this.storageConditions;
    }

    // Effects: get status;
    public String getStatus() {
        return this.status;
    }

    // Effects: helper function sets today's date at 00:00:00;
    private Date getToday() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    // Requires: expiration date later than today.
    // Effects: produces the remaining shelf life of food.
    public int calculateDaysLeft() {
        int days = calculateDifferenceInDays(getToday(), this.expirationDate);
        return days;
    }

    // Requires d2>d1
    // Effects: calculate the days left from the expiration date.
    public int calculateDifferenceInDays(Date d1, Date d2) {
        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();
        date1.setTime(d1);
        date2.setTime(d2);
        long diffInMillis = date2.getTimeInMillis() - date1.getTimeInMillis();
        int daysLeft = (int) TimeUnit.MILLISECONDS.toDays(diffInMillis);
        return daysLeft;
    }

    // REQUIRES: expDate
    // MODIFIES: This
    // EFFECTS: retrieve default shelf life and add it to today's date.
    public void initializeExpirationDate() {
        Calendar expDate = Calendar.getInstance();
        expDate.setTime(this.purchaseDate);
        int day = calculateShelfLife(this.storageConditions.get(0));
        // check date problems, like dec 31 + 7 days.
        expDate.add(Calendar.DATE, day);
        this.expirationDate = expDate.getTime();
    }

    // Requires: Storage condition be in the index table.
    // Effects: produce the default shelf life according to food type and storage condition.
    public int calculateShelfLife(String storageCondition) {
        ReferenceShelfLife refShelf = new ReferenceShelfLife();
        int typeIndex = refShelf.getIndexTable().get(type);
        int storageIndex = refShelf.getIndexTable().get(storageCondition);
        int day = refShelf.getMatrixRep().get(typeIndex)
                .get(storageIndex);
        return day;
    }

    // Requires: food.getStorageCondition are elements from indexTable.
    //           expiration date not empty.
    //           Destination is a valid destination.
    //           Today's date in shelf-life.
    // Modifies: this.
    // Effects: update exp date after a storage transfer.
    public void updateExpDate(Storage destination) {
        int daysLeft = this.calculateDaysLeft();
        int originalShelfLife = this.calculateShelfLife(this.getStorageConditions()
                .get(this.getStorageConditions().size() - 1));
        int newShelfLife = this.calculateShelfLife(destination.getStorageType());
        int day = (int) Math.floor((double) daysLeft / (double) originalShelfLife * (double) newShelfLife);
        Calendar expDate = Calendar.getInstance();
        expDate.setTime(getToday());
        expDate.add(Calendar.DATE, day);
        this.expirationDate = expDate.getTime();
    }

    // Requires: food not expired already.
    // Modifies: this.
    // Effects: set the expiration date to 3 days from now and change the status to open bag.
    public void openBag() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getToday());
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        expirationDate = calendar.getTime();
        status = "Open Bag";
    }

    // Requires: food doesn't have an ID already.
    // modifies: This.
    // Effects: assign a unique ID to food item.
    public int assignID(Inventory inventory) {
        ArrayList<Food> allInventory = inventory.buildAllInventory();
        int foodId = 0;
        for (Food thisFood : allInventory) {
            if (thisFood.getID() > foodId) {
                foodId = thisFood.getID();
            }
        }
        foodId++;
        return foodId;
    }

    @Override
    // Effects: converts a single food item to JSON.
    public JSONObject toJson() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        JSONObject json = new JSONObject();

        //    private HashMap<String, Integer> indexTable = new HashMap<String, Integer>();
        //    private ArrayList<ArrayList<Integer>> matrixRep = new ArrayList<>();
        json.put("name", name);
        json.put("purchaseDate", sdf.format(purchaseDate));
        json.put("expirationDate", sdf.format(expirationDate));
        json.put("quantity", quantity);
        json.put("unit", unit);
        json.put("type", type);
        json.put("status", status);
        json.put("storageConditions", new JSONArray(storageConditions));
        json.put("id", id);

        return json;
    }

}