package io.auto.menu;

import io.auto.enums.FuelType;
import io.auto.enums.RvType;
import io.auto.enums.VehicleCondition;
import io.auto.enums.VehicleType;
import io.auto.interfaces.IMenu;
import io.auto.model.Bay;
import io.auto.service.VehicleService;
import io.auto.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class VehicleMenu implements IMenu {

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private BayMenu  bayMenu;

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> listAllVehicles();
                case 2 -> addVehicle();
                case 3 -> viewVehicleDetail();
                case 4 -> editVehicle();
                case 5 -> moveVehicleToBay();
            }
        }
        while (choice != 0);
    }

    public void moveVehicleToBay() {
    }

    public void editVehicle() {
    }

    public void viewVehicleDetail() {

    }

    public void listAllVehicles() {
        vehicleService.listAllVehicles();
    }

    public void addVehicle() {
        System.out.println("Select a vehicle type (PASSENGER, MOTORCYCLE, RECREATIONAL):");
        VehicleType vehicleType = VehicleType.valueOf(InputHandler.getStringInput().toUpperCase());
        Bay bay = bayMenu.listBaysAndSelect();
        System.out.println("Enter the year:");
        int year = InputHandler.getIntegerInput();
        System.out.println("Enter the make:");
        String make = InputHandler.getStringInput();
        System.out.println("Enter the model:");
        String model = InputHandler.getStringInput();
        System.out.println("Enter the VIN:");
        String vin = InputHandler.getStringInput();
        System.out.println("Enter the purchase price:");
        double purchasePrice = InputHandler.getDoubleInput();
        System.out.println("Enter the purchase date (yyyy-MM-dd):");
        LocalDate purchaseDate = InputHandler.getDateInput();
        System.out.println("Enter the registration expiry date (yyyy-MM-dd):");
        LocalDate registrationExpiryDate = InputHandler.getDateInput();
        System.out.println("Enter the condition (EXCELLENT, GOOD, FAIR, POOR, PARTS_ONLY):");
        VehicleCondition condition = VehicleCondition.valueOf(InputHandler.getStringInput().toUpperCase());
        System.out.println("Enter the notes:");
        String notes = InputHandler.getStringInput();

        switch (vehicleType) {
            case PASSENGER -> {
                System.out.println("Enter the odometer in Km:");
                long odometerKm = InputHandler.getIntegerInput();
                System.out.println("Enter the fuel type (GASOLINE, DIESEL, ELECTRIC, HYBRID , OTHER):");
                FuelType fuelType = FuelType.valueOf(InputHandler.getStringInput().toUpperCase());
                System.out.println("Enter the number of seats:");
                int seats = InputHandler.getIntegerInput();
                System.out.println("Is this electric of hybrid (Y/N)?:");
                boolean isElectricOrHybrid = InputHandler.getBooleanInput();

                vehicleService.addPassengerVehicle(vehicleType, bay, year, make, model, vin, purchasePrice, purchaseDate, registrationExpiryDate, condition, notes, odometerKm, fuelType, seats, isElectricOrHybrid);
                System.out.println("Passenger vehicle has been added successfully!");
            }
            case MOTORCYCLE -> {
                System.out.println("Enter the engineCC:");
                int engineCC = InputHandler.getIntegerInput();
                System.out.println("Does this motorcycle have a sidecar (Y/N)?:");
                boolean hasSideCar = InputHandler.getBooleanInput();
                System.out.println("Enter the odometer in Km:");
                long odometerKm = InputHandler.getIntegerInput();

                vehicleService.addMotorcycleVehicle(vehicleType, bay, year, make, model, vin, purchasePrice, purchaseDate, registrationExpiryDate, condition, notes, engineCC, hasSideCar, odometerKm);
                System.out.println("Motorcycle has been added successfully!");
            }
            case RECREATIONAL -> {
                System.out.println("Enter the RV type:");
                RvType rvType = RvType.valueOf(InputHandler.getStringInput());
                System.out.println("Enter the storage notes:");
                String storageNotes = InputHandler.getStringInput();
                System.out.println("Does this recreational vehicle require a trailer (Y/N)?:");
                boolean requiresTrailer = InputHandler.getBooleanInput();

                vehicleService.addRecreationalVehicle(vehicleType, bay, year, make, model, vin, purchasePrice, purchaseDate, registrationExpiryDate, condition, notes, rvType, storageNotes, requiresTrailer);
                System.out.println("Recreational vehicle has been added successfully!");
            }
        }

    }

    @Override
    public void printOptions() {
        System.out.println("[1] List all vehicles");
        System.out.println("[2] Add vehicle");
        System.out.println("[3] View vehicle detail");
        System.out.println("[4] Edit vehicle");
        System.out.println("[5] Move vehicle to bay");
    }

}
