package io.auto.service;

import io.auto.model.Bay;
import io.auto.repository.BayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BayService {
    @Autowired
    private BayRepository bayRepository;

    public List<Bay> findBaysByUserId(String userId) {
        return bayRepository.findByUserId(userId);
    }

    public List<Bay> findBaysByUserIdAndIsOccupied(String userId, boolean isOccupied) {
        return bayRepository.findByUserIdAndIsOccupied(userId, isOccupied);
    }

}
