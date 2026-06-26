package io.auto.repository;

import io.auto.model.MaintenanceRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MaintenanceRepository extends MongoRepository<MaintenanceRecord, String> {
    List<MaintenanceRecord> findByVehicleId(String vehicleId);
    List<MaintenanceRecord> findByVehicleIdOrderByServiceDateDesc(String userId);
}
