package io.auto.alert;

import io.auto.enums.AlertType;
import io.auto.interfaces.AlertStrategy;
import io.auto.model.Vehicle;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RegistrationExpiryStrategy implements AlertStrategy {
    @Override
    public boolean supports(Vehicle vehicle) {
        return (vehicle != null) && (vehicle.getRegistrationExpiryDate() != null);
    }

    @Override
    public AlertResult evaluate(Vehicle vehicle) {
        AlertResult alertResult = null;
        if(ChronoUnit.DAYS.between(vehicle.getRegistrationExpiryDate(), LocalDate.now()) <= 30) {
            alertResult = new AlertResult(vehicle, AlertType.REGISTRATION_EXPIRY, "The registration will expiry on " + vehicle.getRegistrationExpiryDate().toString());
        }
        return alertResult;
    }
}
