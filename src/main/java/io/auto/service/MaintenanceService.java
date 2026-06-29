package io.auto.service;

import io.auto.enums.ServiceType;
import io.auto.model.MaintenanceRecord;
import io.auto.model.Vehicle;
import io.auto.repository.MaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MaintenanceService {

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    public List<MaintenanceRecord> findByVehicleId(String vehicleId) {
        return maintenanceRepository.findByVehicleId(vehicleId);
    }

    public void addMaintenanceRecord(Vehicle vehicle, LocalDate serviceDate, ServiceType serviceType, String description, double cost, long mileage, String technician) {
    MaintenanceRecord maintenanceRecord = new MaintenanceRecord(vehicle.getId(), serviceDate, serviceType, description, cost, mileage, technician);
    }
}
