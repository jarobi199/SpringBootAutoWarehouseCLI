package io.auto.depreciation;

import io.auto.interfaces.DepreciationStrategy;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SeasonalDepreciationStrategy implements DepreciationStrategy {

    @Override
    public double calculate(double purchasePrice, LocalDate purchaseDate) {
        long yearsOwned = ChronoUnit.YEARS.between(purchaseDate, LocalDate.now());
        return purchasePrice * Math.max(0.10, 1 - (0.25 * Math.min(yearsOwned, 2)) -  (0.05 * Math.max(0, yearsOwned - 2)));
    }
}
