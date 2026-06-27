package io.auto.model;

import io.auto.enums.BayType;
import org.springframework.data.annotation.Id;

public class Bay {
    @Id
    protected String id;
    protected String userId;
    protected int bayNumber;
    protected String name;
    protected BayType bayType;
    protected boolean isOccupied;
    protected String notes;

    public Bay() {
        //No argument constructor
    }

    public Bay(String userId, int bayNumber, String name, BayType bayType, boolean isOccupied, String notes) {
        this.userId = userId;
        this.bayNumber = bayNumber;
        this.name = name;
        this.bayType = bayType;
        this.isOccupied = isOccupied;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getBayNumber() {
        return bayNumber;
    }

    public void setBayNumber(int bayNumber) {
        this.bayNumber = bayNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BayType getBayType() {
        return bayType;
    }

    public void setBayType(BayType bayType) {
        this.bayType = bayType;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDisplay() {
        return "Bay #" + bayNumber + " - " + name + " (" + bayType.name() + ")";
    }
}
