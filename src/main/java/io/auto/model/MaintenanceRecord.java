package io.auto.model;

import io.auto.enums.ServiceType;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

public class MaintenanceRecord {
    @Id
    private String id;
    private String vehicleId;
    private LocalDate serviceDate;
    private ServiceType serviceType;
    private String description;
    private double cost;
    private long mileage;
    private String technician;

    public MaintenanceRecord() {
        //No argument constructor
    }

    public MaintenanceRecord(String vehicleId, LocalDate serviceDate, ServiceType serviceType, String description, double cost, long mileage, String technician) {
        this.vehicleId = vehicleId;
        this.serviceDate = serviceDate;
        this.serviceType = serviceType;
        this.description = description;
        this.cost = cost;
        this.mileage = mileage;
        this.technician = technician;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LocalDate getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public long getMileage() {
        return mileage;
    }

    public void setMileage(long mileage) {
        this.mileage = mileage;
    }

    public String getTechnician() {
        return technician;
    }

    public void setTechnician(String technician) {
        this.technician = technician;
    }
}
