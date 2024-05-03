package model;

import persistence.Writable;

// Freezer is a storage space where food shelf life is prolonged. Food that's frozen there cannot be refrozen.
public class Freezer extends Storage {


    public Freezer() {
        super();
        this.storageType = "freezer";
    }
}
