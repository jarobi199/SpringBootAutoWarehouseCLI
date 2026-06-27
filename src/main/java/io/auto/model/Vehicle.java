package io.auto.model;

import io.auto.enums.VehicleCondition;
import io.auto.enums.VehicleType;
import io.auto.interfaces.DepreciationStrategy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.time.LocalDate;

public abstract class Vehicle {
    @Id
    protected String id;
    protected String userId;
    protected String bayId;
    protected String make;
    protected String model;
    protected int year;
    protected String vin;
    protected double purchasePrice;
    protected LocalDate purchaseDate;
    protected LocalDate registrationExpiryDate;
    protected VehicleCondition condition;
    protected String notes;
    @Transient
    protected DepreciationStrategy depreciationStrategy;

    public Vehicle(String userId, String bayId, String make, String model, int year, String vin, double purchasePrice, LocalDate purchaseDate, LocalDate registrationExpiryDate, VehicleCondition condition, String notes) {
        this.userId = userId;
        this.bayId = bayId;
        this.make = make;
        this.model = model;
        this.year = year;
        this.vin = vin;
        this.purchasePrice = purchasePrice;
        this.purchaseDate = purchaseDate;
        this.registrationExpiryDate = registrationExpiryDate;
        this.condition = condition;
        this.notes = notes;
    }

    public Vehicle() {
        //No argument constructor
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

    public String getBayId() {
        return bayId;
    }

    public void setBayId(String bayId) {
        this.bayId = bayId;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LocalDate getRegistrationExpiryDate() {
        return registrationExpiryDate;
    }

    public void setRegistrationExpiryDate(LocalDate registrationExpiryDate) {
        this.registrationExpiryDate = registrationExpiryDate;
    }

    public VehicleCondition getCondition() {
        return condition;
    }

    public void setCondition(VehicleCondition condition) {
        this.condition = condition;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getVehicleDisplay() {
        return year + " " + make + " " + model;
    }

    public abstract VehicleType getVehicleType();

    public abstract double calculateDepreciatedValue();
}
