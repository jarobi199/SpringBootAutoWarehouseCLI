package io.auto.model;

import io.auto.enums.RvType;
import io.auto.enums.VehicleCondition;
import io.auto.enums.VehicleType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecreationalVehicle extends Vehicle {
    private RvType rvType;
    private String storageNotes;
    private boolean requiresTrailer;
    private List<SeasonalRecord> usageLogs;

    public RecreationalVehicle() {
        //No argument constructor
    }

    public RecreationalVehicle(String userId, String bayId, String make, String model, int year, String vin, double purchasePrice, LocalDate purchaseDate, LocalDate registrationExpiryDate, VehicleCondition condition, String notes, boolean requiresTrailer, String storageNotes, RvType rvType) {
        super(userId, bayId, make, model, year, vin, purchasePrice, purchaseDate, registrationExpiryDate, condition, notes);
        this.requiresTrailer = requiresTrailer;
        this.storageNotes = storageNotes;
        this.rvType = rvType;
        this.usageLogs = new ArrayList<>();
    }

    public RvType getRvType() {
        return rvType;
    }

    public void setRvType(RvType rvType) {
        this.rvType = rvType;
    }

    public String getStorageNotes() {
        return storageNotes;
    }

    public void setStorageNotes(String storageNotes) {
        this.storageNotes = storageNotes;
    }

    public boolean isRequiresTrailer() {
        return requiresTrailer;
    }

    public void setRequiresTrailer(boolean requiresTrailer) {
        this.requiresTrailer = requiresTrailer;
    }

    public List<SeasonalRecord> getUsageLogs() {
        return usageLogs;
    }

    public void setUsageLogs(List<SeasonalRecord> usageLogs) {
        this.usageLogs = usageLogs;
    }

    @Override
    public VehicleType getVehicleType() {
        return VehicleType.RECREATIONAL;
    }

    @Override
    public double calculateDepreciatedValue() {
        return 0;
    }
}
