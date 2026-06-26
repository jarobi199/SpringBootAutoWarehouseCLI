package io.auto.interfaces;

import io.auto.alert.AlertResult;
import io.auto.model.Vehicle;

public interface AlertStrategy {
    boolean supports(Vehicle vehicle);
    AlertResult evaluate(Vehicle vehicle);
}
