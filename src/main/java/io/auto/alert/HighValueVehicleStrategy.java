package io.auto.alert;

import io.auto.authentication.SessionContext;
import io.auto.enums.AlertType;
import io.auto.interfaces.AlertStrategy;
import io.auto.model.Vehicle;

public class HighValueVehicleStrategy implements AlertStrategy {
    @Override
    public boolean supports(Vehicle vehicle) {
        return (vehicle != null);
    }

    @Override
    public AlertResult evaluate(Vehicle vehicle) {
        AlertResult alertResult = null;
        if(vehicle.getPurchasePrice() > SessionContext.getUser().getHighValueThreshold()) {
            alertResult = new AlertResult(vehicle, AlertType.HIGH_VALUE_VEHICLE, "This is a high value vehicle! Please verify that you have adequate insurance coverage.");
        }

        return alertResult;
    }
}
