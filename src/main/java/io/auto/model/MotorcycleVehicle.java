package io.auto.model;

import io.auto.depreciation.LinearlDepreciationStrategy;
import io.auto.enums.VehicleCondition;
import io.auto.enums.VehicleType;

import java.time.LocalDate;

public class MotorcycleVehicle extends Vehicle {
    private int engineCC;
    private boolean hasSideCar;
    private long odometerKm;

    public MotorcycleVehicle() {
       this.depreciationStrategy = new LinearlDepreciationStrategy();
    }

    public MotorcycleVehicle(String userId, String bayId, String make, String model, int year, String vin, double purchasePrice, LocalDate purchaseDate, LocalDate registrationExpiryDate, VehicleCondition condition, String notes, int engineCC, boolean hasSideCar, long odometerKm) {
        super(userId, bayId, make, model, year, vin, purchasePrice, purchaseDate, registrationExpiryDate, condition, notes);
        this.engineCC = engineCC;
        this.hasSideCar = hasSideCar;
        this.odometerKm = odometerKm;
    }

    public int getEngineCC() {
        return engineCC;
    }

    public void setEngineCC(int engineCC) {
        this.engineCC = engineCC;
    }

    public boolean isHasSideCar() {
        return hasSideCar;
    }

    public void setHasSideCar(boolean hasSideCar) {
        this.hasSideCar = hasSideCar;
    }

    public long getOdometerKm() {
        return odometerKm;
    }

    public void setOdometerKm(long odometerKm) {
        this.odometerKm = odometerKm;
    }

    @Override
    public VehicleType getVehicleType() {
        return VehicleType.MOTORCYCLE;
    }

    @Override
    public double calculateDepreciatedValue() {
        return depreciationStrategy.calculate(purchasePrice, purchaseDate);
    }
}
