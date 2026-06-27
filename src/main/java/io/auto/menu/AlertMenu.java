package io.auto.menu;

import io.auto.enums.AlertType;
import io.auto.interfaces.IMenu;
import io.auto.service.AlertService;
import io.auto.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlertMenu implements IMenu {

    @Autowired
    private AlertService alertService;

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> viewAllAlerts();
                case 2 -> registrationExpiryAlerts();
                case 3 -> maintenanceOverdueAlerts();
                case 4 -> highValueItems();
            }
        }
        while (choice != 0);
    }

    public void highValueItems() {
        alertService.displayAlerts(AlertType.HIGH_VALUE_VEHICLE);
    }

    public void registrationExpiryAlerts() {
        alertService.displayAlerts(AlertType.REGISTRATION_EXPIRY);
    }

    public void maintenanceOverdueAlerts() {
        alertService.displayAlerts(AlertType.MAINTENANCE_OVERDUE);
    }

    public void viewAllAlerts() {
        alertService.displayAlerts(null);
    }

    @Override
    public void printOptions() {
        System.out.println("[1] View all alerts");
        System.out.println("[2] Registration expiry alerts");
        System.out.println("[3] Maintenance overdue alerts");
        System.out.println("[4] High value vehicle alerts");
        System.out.println("[0] Back");
    }
}
