package io.auto.menu;

import io.auto.enums.ServiceType;
import io.auto.interfaces.IMenu;
import io.auto.model.Vehicle;
import io.auto.service.MaintenanceService;
import io.auto.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MaintenanceMenu implements IMenu {

    @Autowired
    private VehicleMenu vehicleMenu;
    @Autowired
    private MaintenanceService maintenanceService;

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> logMaintenanceRecord();
                case 2 -> viewHistoryByVehicle();
                case 3 -> viewAllRecentRecords();
                case 4 -> deleteRecord();
            }
        }
        while (choice != 0);
    }

    public void deleteRecord() {
    }

    public void viewAllRecentRecords() {
    }

    public void viewHistoryByVehicle() {
    }

    public void logMaintenanceRecord() {
        Vehicle vehicle = vehicleMenu.listVehiclesAndSelect();

        System.out.println("Enter the service date (yyyy-MM-dd):");
        LocalDate serviceDate = InputHandler.getDateInput();
        System.out.println("Enter the service type (OIL_CHANGE , TIRE_SERVICE, BRAKE_SERVICE, ENGINE_REPAIR, BODYWORK, INSPECTION, DETAILING , OTHER):");
        ServiceType serviceType = ServiceType.valueOf(InputHandler.getStringInput());
        System.out.println("Enter the description");
        String description = InputHandler.getStringInput();
        System.out.println("Enter the cost:");
        double cost = InputHandler.getDoubleInput();
        System.out.println("Enter the mileage at service time:");
        long mileage = InputHandler.getIntegerInput();
        System.out.println("Enter the name of the technician:");
        String technician = InputHandler.getStringInput();

        maintenanceService.addMaintenanceRecord(vehicle, serviceDate, serviceType, description, cost, mileage, technician);
        System.out.println("Maintenance record has been added successfully.");
    }

    @Override
    public void printOptions() {
        System.out.println("[1] Log maintenance record");
        System.out.println("[2] View history by vehicle");
        System.out.println("[3] View all recent records");
        System.out.println("[4] Delete record");
        System.out.println("[0] Back");
    }
}
