package io.auto.interfaces;

import java.time.LocalDate;

public interface DepreciationStrategy {
    double calculate(double purchasePrice, LocalDate purchaseDate);
}
