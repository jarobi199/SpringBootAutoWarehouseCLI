package io.auto.menu;

import io.auto.interfaces.IMenu;
import io.auto.model.Vehicle;
import io.auto.service.ReportService;
import io.auto.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportMenu implements IMenu {

    @Autowired
    private VehicleMenu vehicleMenu;
    @Autowired
    private ReportService reportService;

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> reportService.fleetSummary();
                case 2 -> reportService.vehicleValuationReport();
                case 3 -> costOfOwnershipReport();
                case 4 -> reportService.maintenanceCostByType();
                case 5 -> reportService.bayOccupancyReport();
            }
        }
        while (choice != 0);
    }

    public void costOfOwnershipReport() {
        Vehicle vehicle = vehicleMenu.listVehiclesAndSelect();
        reportService.costOfOwnership(vehicle);
    }

    @Override
    public void printOptions() {
        System.out.println();
        System.out.println("| REPORTS MENU |");
        System.out.println("[1] Fleet summary");
        System.out.println("[2] Vehicle valuation report");
        System.out.println("[3] Cost-of-ownership report");
        System.out.println("[4] Maintenance cost by type");
        System.out.println("[5] Bay occupancy report");
        System.out.println("[0] Back");
    }
}


