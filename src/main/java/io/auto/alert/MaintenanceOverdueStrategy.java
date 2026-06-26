package io.auto.alert;

import io.auto.interfaces.AlertStrategy;
import io.auto.model.Vehicle;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class MaintenanceOverdueStrategy implements AlertStrategy {
    @Override
    public boolean supports(Vehicle vehicle) {
        return (vehicle != null);
    }

    @Override
    public AlertResult evaluate(Vehicle vehicle) {
        AlertResult alertResult = null;
        long vehicleAge = ChronoUnit.YEARS.between(vehicle.getPurchaseDate(), LocalDate.now());

        return alertResult;
    }
}
