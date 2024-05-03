package persistence;


import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Inventory read(Inventory inventory) throws IOException, ParseException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseInventory(inventory, jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses workroom from JSON object and returns it
    private void parseFood(Inventory inventory, JSONObject jsonObject) throws ParseException {

        Date purchaseDate = null;

        String name = jsonObject.getString("name");
        try {
            purchaseDate = parseDate(jsonObject.getString("purchaseDate"));

        } catch (ParseException e) {
            throw new ParseException("Purchase Date can't be parsed correctly.",1);
        }
        String unit = jsonObject.getString("unit");
        double quantity = jsonObject.getDouble("quantity");
        int id = jsonObject.getInt("id");
        String type = jsonObject.getString("type");
        Storage storageCondition = parseStorageCondition(inventory, jsonObject);
        String status = jsonObject.getString("status");

        Food food = inventory.makeFood(name, purchaseDate, quantity, unit, type, storageCondition);

        Date expirationDate = null;
        try {
            expirationDate = parseDate(jsonObject.getString("expirationDate"));
        } catch (ParseException e) {
            throw new ParseException("Expiration Date can't be parsed correctly.",2);
        }
        ArrayList<String> storageConditions = parseStorageConditions(jsonObject);
        food.setExpirationDate(expirationDate);
        food.setStorageConditions(storageConditions);
    }

    // Effects: parse the JSON file and create food items in the inventory.
    private Inventory parseInventory(Inventory inventory, JSONObject jsonObject) throws ParseException {

        JSONArray foodArray = new JSONArray();
        foodArray = jsonObject.getJSONArray("inventory");

        for (int i = 0; i < foodArray.length(); i++) {
            JSONObject foodObject = new JSONObject();
            foodObject = foodArray.getJSONObject(i);
            parseFood(inventory, foodObject);
        }
        EventLog.getInstance().logEvent(new Event("Inventory loaded."));
        return inventory;
    }

    // Effects: helper function that parses Date string to Date.
    private Date parseDate(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateString);
    }

    // Effects: helper function that convert storage string to the actual storage condition.
    private Storage parseStorageCondition(Inventory inventory, JSONObject jsonObject) {
        ArrayList<String> storageConditions = this.parseStorageConditions(jsonObject);
        String lastCondition = storageConditions.get(storageConditions.size() - 1);
        Storage storageCondition = new Pantry();
        if (lastCondition.equals("pantry")) {
            storageCondition = inventory.getPantry();
        } else if (lastCondition.equals("fridge")) {
            storageCondition = inventory.getFridge();
        } else if (lastCondition.equals("freezer")) {
            storageCondition = inventory.getFreezer();
        }
        return storageCondition;
    }

    // Effects: parse Storage conditions from JSON to arrayList.
    private ArrayList<String> parseStorageConditions(JSONObject jsonObject) {

        JSONArray storageConditionsArray = jsonObject.getJSONArray("storageConditions");

        ArrayList<String> storageConditions = new ArrayList<>();
        for (int i = 0; i < storageConditionsArray.length(); i++) {
            storageConditions.add(storageConditionsArray.getString(i));
        }

        return storageConditions;
    }

//    // MODIFIES: wr
//    // EFFECTS: parses thingies from JSON object and adds them to workroom
//    private void addThingies(WorkRoom wr, JSONObject jsonObject) {
//        JSONArray jsonArray = jsonObject.getJSONArray("thingies");
//        for (Object json : jsonArray) {
//            JSONObject nextThingy = (JSONObject) json;
//            addThingy(wr, nextThingy); // write elements
//        }
//    }

//    // MODIFIES: wr
//    // EFFECTS: parses thingy from JSON object and adds it to workroom
//    private void addThingy(WorkRoom wr, JSONObject jsonObject) {
//        String name = jsonObject.getString("name");
//        Category category = Category.valueOf(jsonObject.getString("category"));
//        Thingy thingy = new Thingy(name, category);
//        wr.addThingy(thingy);
//    }
}
