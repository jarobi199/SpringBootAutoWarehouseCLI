package io.auto.menu;

import io.auto.interfaces.IMenu;
import io.auto.service.ReportService;
import io.auto.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportMenu implements IMenu {

    @Autowired
    private ReportService reportService;

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> fleetSummary();
                case 2 -> vehicleValuationReport();
                case 3 -> costOfOwnershipReport();
                case 4 -> maintenanceCostByType();
                case 5 -> bayOccupancyReport();
            }
        }
        while (choice != 0);
    }

    private void fleetSummary() {
        reportService.fleetSummary();
    }

    public void bayOccupancyReport() {
    }

    private void maintenanceCostByType() {
    }

    public void costOfOwnershipReport() {

    }

    public void vehicleValuationReport() {

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


