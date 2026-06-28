package io.auto.repository;

import io.auto.model.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VehicleRepository extends MongoRepository<Vehicle, String> {
    List<Vehicle> findByUserId(String userId);
    Vehicle findByBayId(String bayId);
}
