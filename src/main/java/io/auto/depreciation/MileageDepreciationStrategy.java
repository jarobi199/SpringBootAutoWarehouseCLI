package io.auto.depreciation;

import io.auto.interfaces.DepreciationStrategy;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class MileageDepreciationStrategy implements DepreciationStrategy {

    @Override
    public double calculate(double purchasePrice, LocalDate purchaseDate) {
        long yearsOwned = ChronoUnit.YEARS.between(purchaseDate, LocalDate.now());
       return 1 - (0.15 * yearsOwned);
    }
}
