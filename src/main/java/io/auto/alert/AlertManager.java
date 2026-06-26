package io.auto.alert;

import io.auto.interfaces.AlertStrategy;
import io.auto.model.Vehicle;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class AlertManager {

    private final List<AlertStrategy> strategies;

    public AlertManager(List<AlertStrategy> strategies) {
        this.strategies = strategies;
    }

    public List<AlertResult> evaluate(List<Vehicle> vehicles) {
        return vehicles.stream()
            .flatMap(item -> strategies.stream()
                .filter(s -> s.supports(item))
                .map(s -> s.evaluate(item)))
                .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}
