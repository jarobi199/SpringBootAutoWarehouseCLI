package io.auto.alert;

import io.auto.enums.AlertType;
import io.auto.model.Vehicle;

public record AlertResult (Vehicle vehicle, AlertType alertType, String message){}
