package io.auto.repository;

import io.auto.enums.BayType;
import io.auto.model.Bay;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BayRepository extends MongoRepository<Bay, String> {
    List<Bay> findByUserId(String userId);
    List<Bay> findByUserIdAndIsOccupied(String userId,  boolean isOccupied);
    List<Bay> findByUserIdAndBayType(String userId,  BayType bayType);
}
