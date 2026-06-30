package io.auto.alert;

import io.auto.enums.AlertType;
import io.auto.interfaces.AlertStrategy;
import io.auto.model.MaintenanceRecord;
import io.auto.model.Vehicle;
import io.auto.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

@Component
public class MaintenanceOverdueStrategy implements AlertStrategy {

    @Autowired
    private  MaintenanceService maintenanceService;

    @Override
    public boolean supports(Vehicle vehicle) {
        return (vehicle != null);
    }

    @Override
    public AlertResult evaluate(Vehicle vehicle) {
        AlertResult alertResult = null;

        long vehicleAge = ChronoUnit.YEARS.between(vehicle.getPurchaseDate(), LocalDate.now());
        MaintenanceRecord maintenanceRecord = maintenanceService.findByVehicleId(vehicle.getId())
                .stream().max(Comparator.comparing(MaintenanceRecord::getServiceDate)).orElse(null);

        if(((maintenanceRecord == null) && (vehicleAge > 1)) || ( (maintenanceRecord != null)  && maintenanceRecordAgeGreaterThanOne(maintenanceRecord))) {
            alertResult = new AlertResult(vehicle, AlertType.MAINTENANCE_OVERDUE, (maintenanceRecord == null) ? "This vehicle has not been serviced!" : "The last service date of this vehicle was " + maintenanceRecord.getServiceDate().toString());
        }
        return alertResult;
    }

    private boolean maintenanceRecordAgeGreaterThanOne(MaintenanceRecord maintenanceRecord) {
        long maintenanceRecordAge = ChronoUnit.YEARS.between(maintenanceRecord.getServiceDate(), LocalDate.now());
        return (maintenanceRecordAge > 1);
    }

}
