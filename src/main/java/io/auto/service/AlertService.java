package io.auto.service;

import io.auto.alert.AlertManager;
import io.auto.alert.AlertResult;
import io.auto.authentication.SessionContext;
import io.auto.enums.AlertType;
import io.auto.model.Vehicle;
import io.auto.repository.VehicleRepository;
import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private AlertManager alertManager;


    public void displayAlerts(AlertType alertType) {
        List<Vehicle> vehicles = vehicleRepository.findByUserId(SessionContext.getUser().getId());
        List<AlertResult> results = alertManager.evaluate(vehicles);
        if(alertType != null) {
            results = results.stream().filter(result -> result.alertType().equals(alertType)).toList();
        }
        displayAlertResults(results);
    }

    public void displayAlertResults(List<AlertResult> alertResults) {
        if(alertResults.isEmpty()) {
            System.out.println("No alerts found.\n");
        }
        else
        {
            System.out.println("ALERTS");
            Table table = Clique.table(TableType.BOX_DRAW)
                    .headers(
                            "[*blue, bold]ALERT TYPE[/]",
                            "[*blue, bold]VEHICLE[/]",
                            "[*blue, bold]MESSAGE[/]"
                    );
            for (AlertResult alertResult : alertResults) {
                table.row(alertResult.alertType().getDisplayName(), alertResult.vehicle().getVehicleDisplay(),  alertResult.message());
            }
            table.render();
        }
    }
}
