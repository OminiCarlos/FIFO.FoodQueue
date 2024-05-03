package model;

import java.util.ArrayList;
import java.util.HashMap;

// a 2X3 reference table for shelf life at different conditions.
public class ReferenceShelfLife {

    private HashMap<String, Integer> indexTable = new HashMap<String, Integer>();
    private ArrayList<ArrayList<Integer>> matrixRep = new ArrayList<>();

    public ReferenceShelfLife() {
        initializeExpTable();
        mapTypeAndConditions();
    }

    // MODIFIES: This
    // Effects: initialize a map storing the default shelf life.
    public void initializeExpTable() {
        ArrayList<Integer> perishable = new ArrayList<>();
        perishable.add(3); // regular
        perishable.add(5); // fridge
        perishable.add(30); // freezer

        ArrayList<Integer> nonPerishable = new ArrayList<>();
        nonPerishable.add(90); // regular
        nonPerishable.add(90); // fridge
        nonPerishable.add(90); // freezer

        matrixRep.add(perishable);
        matrixRep.add(nonPerishable);
    }

    // Modifies: this;
    // Effects: generate a map for the factors that determines the shelf life.
    public void mapTypeAndConditions() {
        indexTable.put("perishable", 0);
        indexTable.put("non-perishable", 1);
        indexTable.put("pantry", 0);
        indexTable.put("fridge", 1);
        indexTable.put("freezer", 2);
    }

    public HashMap<String, Integer> getIndexTable() {
        return indexTable;
    }

    public ArrayList<ArrayList<Integer>> getMatrixRep() {
        return matrixRep;
    }
}
