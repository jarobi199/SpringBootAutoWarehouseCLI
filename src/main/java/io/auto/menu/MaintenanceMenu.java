package io.auto.menu;

import com.sun.tools.javac.Main;
import io.auto.authentication.SessionContext;
import io.auto.enums.ServiceType;
import io.auto.interfaces.IMenu;
import io.auto.model.MaintenanceRecord;
import io.auto.model.Vehicle;
import io.auto.service.MaintenanceService;
import io.auto.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

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
                case 3 -> deleteRecord();
            }
        }
        while (choice != 0);
    }

    public void deleteRecord() {
        Vehicle vehicle = vehicleMenu.listVehiclesAndSelect();
        List<MaintenanceRecord> maintenanceRecordList = maintenanceService.findByVehicleId(vehicle.getId());
        MaintenanceRecord maintenanceRecord = listMaintenanceRecordsAndSelect(maintenanceRecordList);
        System.out.println("Are you sure you want to delete maintenance record (Y/N)?");
        String answer = InputHandler.getStringInput();
        if (answer.equalsIgnoreCase("Y")) {
            maintenanceService.deleteMaintenanceRecord(maintenanceRecord);
            System.out.println("Maintenance record deleted successfully");
        }
    }

    public void viewHistoryByVehicle() {
        Vehicle vehicle = vehicleMenu.listVehiclesAndSelect();
        maintenanceService.viewMaintenanceRecords(vehicle);
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

    public MaintenanceRecord listMaintenanceRecordsAndSelect(List<MaintenanceRecord> maintenanceRecordList) {
        int number = 1;
        MaintenanceRecord maintenanceRecord = null;
        int choice = 0;

        if (!maintenanceRecordList.isEmpty()) {
            for (MaintenanceRecord m : maintenanceRecordList) {
                System.out.println("[" + number + "] " +  m.getDescription() + " (" + m.getServiceType().toString() + ")");
                number++;
            }
            System.out.println("Select a maintenance record:");
            choice = InputHandler.getIntegerInput();
            maintenanceRecord = maintenanceRecordList.get(choice - 1);
        }
        else
        {
            System.out.println("There are no maintenance records available.");
        }

        return maintenanceRecord;
    }
    @Override
    public void printOptions() {
        System.out.println();
        System.out.println("[1] Log maintenance record");
        System.out.println("[2] View history by vehicle");
        System.out.println("[3] Delete record");
        System.out.println("[0] Back");
    }
}
