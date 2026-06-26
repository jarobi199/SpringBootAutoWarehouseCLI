package io.auto.model;

import io.auto.depreciation.MileageDepreciationStrategy;
import io.auto.enums.FuelType;
import io.auto.enums.VehicleCondition;
import io.auto.enums.VehicleType;

import java.time.LocalDate;

public class PassengerVehicle extends Vehicle {
    private long odometerKm;
    private FuelType fuelType;
    private int numberOfSeats;
    private boolean  isElectricOrHybrid;

    public PassengerVehicle() {
        this.depreciationStrategy = new MileageDepreciationStrategy();
    }

    public PassengerVehicle(String userId, String bayId, String make, String model, int year, String vin, double purchasePrice, LocalDate purchaseDate, LocalDate registrationExpiryDate, VehicleCondition condition, String notes, long odometerKm, boolean isElectricOrHybrid, int numberOfSeats, FuelType fuelType) {
        super(userId, bayId, make, model, year, vin, purchasePrice, purchaseDate, registrationExpiryDate, condition, notes);
        this.odometerKm = odometerKm;
        this.isElectricOrHybrid = isElectricOrHybrid;
        this.numberOfSeats = numberOfSeats;
        this.fuelType = fuelType;
    }

    public long getOdometerKm() {
        return odometerKm;
    }

    public void setOdometerKm(long odometerKm) {
        this.odometerKm = odometerKm;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public boolean isElectricOrHybrid() {
        return isElectricOrHybrid;
    }

    public void setElectricOrHybrid(boolean electricOrHybrid) {
        isElectricOrHybrid = electricOrHybrid;
    }

    @Override
    public VehicleType getVehicleType() {
       return VehicleType.PASSENGER;
    }

    public double calculateDepreciatedValue() {
        double ageAdjusted = depreciationStrategy.calculate(purchasePrice, purchaseDate);
        double mileagePenalty = purchasePrice * 0.00001 * odometerKm;
        return Math.max(purchasePrice * 0.15, ageAdjusted - mileagePenalty);
    }

}
