package persistence;

import org.json.JSONObject;

// tag for the classes that are to be saved in the JSON file.
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
