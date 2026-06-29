package io.auto.menu;

import io.auto.authentication.SessionContext;
import io.auto.enums.FuelType;
import io.auto.enums.RvType;
import io.auto.enums.VehicleCondition;
import io.auto.enums.VehicleType;
import io.auto.interfaces.IMenu;
import io.auto.model.Bay;
import io.auto.model.RecreationalVehicle;
import io.auto.model.Vehicle;
import io.auto.service.VehicleService;
import io.auto.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class VehicleMenu implements IMenu {

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private BayMenu bayMenu;

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
                case 6 -> addSeasonalRecord();
                case 7 -> deleteVehicle();
            }
        }
        while (choice != 0);
    }

    public void deleteVehicle() {
        Vehicle vehicle = listVehiclesAndSelect();
        System.out.println("Are you sure you want to delete this vehicle (Y/N)?:");
        String answer = InputHandler.getStringInput();
        if ("Y".equalsIgnoreCase(answer)) {
            vehicleService.deleteVehicle(vehicle);
            System.out.println("Vehicle has been deleted!");
        }
    }

    public void addSeasonalRecord() {
        RecreationalVehicle recreationalVehicle = (RecreationalVehicle) listVehiclesAndSelect(VehicleType.RECREATIONAL);
        if (recreationalVehicle != null) {
            System.out.println("Enter the season:");
            String season = InputHandler.getStringInput();
            System.out.println("Enter the hours used:");
            int hoursUsed = InputHandler.getIntegerInput();
            System.out.println("Enter the bay number:");
            int bayLocation = InputHandler.getIntegerInput();
            System.out.println("Enter the notes:");
            String notes = InputHandler.getStringInput();

            vehicleService.addSeasonalRecord(recreationalVehicle, season, hoursUsed, bayLocation, notes);
            System.out.println("Seasonal record added successfully!");
        }
    }

    public void moveVehicleToBay() {
        Vehicle vehicle = listVehiclesAndSelect();
        Bay newBay = bayMenu.listBaysAndSelect();
        vehicleService.moveVehicle(vehicle, newBay);

        System.out.println("The vehicle has been moved to the new bay: " + newBay.getName());
    }

    public void editVehicle() {
        Vehicle vehicle = listVehiclesAndSelect();
        if (vehicle != null) {
            System.out.println("Enter the registration expiry date (yyyy-MM-dd):");
            LocalDate newRegistrationExpiryDate = InputHandler.getDateInput();
            System.out.println("Enter the condition (EXCELLENT, GOOD, FAIR, POOR, PARTS_ONLY):");
            VehicleCondition newCondition = VehicleCondition.valueOf(InputHandler.getStringInput().toUpperCase());
            System.out.println("Enter the notes:");
            String newNotes = InputHandler.getStringInput();
            String newStorageNotes = null;
            int newEngineCC = 0;
            long newOdometerKm = 0;
            FuelType newFuelType = null;

            switch (vehicle.getVehicleType()) {
                case RECREATIONAL -> {
                    System.out.println("Enter the storage notes:");
                    newStorageNotes = InputHandler.getStringInput();
                }
                case MOTORCYCLE ->  {
                    System.out.println("Enter the engine CC:");
                    newEngineCC = InputHandler.getIntegerInput();
                }
                case PASSENGER ->  {
                    System.out.println("Enter the odometer in Km:");
                    newOdometerKm = InputHandler.getIntegerInput();
                    System.out.println("Enter the fuel type (GASOLINE, DIESEL, ELECTRIC, HYBRID , OTHER):");
                    newFuelType = FuelType.valueOf(InputHandler.getStringInput().toUpperCase());
                }
            }

            vehicleService.editVehicle(vehicle, newRegistrationExpiryDate, newCondition, newNotes, newStorageNotes, newEngineCC, newOdometerKm, newFuelType);
            System.out.println("Vehicle has been edited successfully!");
        }
    }

    public void viewVehicleDetail() {
        Vehicle vehicle = listVehiclesAndSelect();
        vehicleService.viewDetails(vehicle);
    }

    public void listAllVehicles() {
        vehicleService.listAllVehicles();
    }

    public void addVehicle() {
        Bay bay = bayMenu.listBaysAndSelect();
        if (bay != null) {
            System.out.println("Select a vehicle type (PASSENGER, MOTORCYCLE, RECREATIONAL):");
            VehicleType vehicleType = VehicleType.valueOf(InputHandler.getStringInput().toUpperCase());
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

                    vehicleService.addPassengerVehicle(bay, year, make, model, vin, purchasePrice, purchaseDate, registrationExpiryDate, condition, notes, odometerKm, fuelType, seats, isElectricOrHybrid);
                    System.out.println("Passenger vehicle has been added successfully!");
                }
                case MOTORCYCLE -> {
                    System.out.println("Enter the engineCC:");
                    int engineCC = InputHandler.getIntegerInput();
                    System.out.println("Does this motorcycle have a sidecar (Y/N)?:");
                    boolean hasSideCar = InputHandler.getBooleanInput();
                    System.out.println("Enter the odometer in Km:");
                    long odometerKm = InputHandler.getIntegerInput();

                    vehicleService.addMotorcycleVehicle(bay, year, make, model, vin, purchasePrice, purchaseDate, registrationExpiryDate, condition, notes, engineCC, hasSideCar, odometerKm);
                    System.out.println("Motorcycle has been added successfully!");
                }
                case RECREATIONAL -> {
                    System.out.println("Enter the RV type (BOAT, RV, ATV, JET_SKI, OTHER):");
                    RvType rvType = RvType.valueOf(InputHandler.getStringInput().toUpperCase());
                    System.out.println("Enter the storage notes:");
                    String storageNotes = InputHandler.getStringInput();
                    System.out.println("Does this recreational vehicle require a trailer (Y/N)?:");
                    boolean requiresTrailer = InputHandler.getBooleanInput();

                    vehicleService.addRecreationalVehicle(bay, year, make, model, vin, purchasePrice, purchaseDate, registrationExpiryDate, condition, notes, rvType, storageNotes, requiresTrailer);
                    System.out.println("Recreational vehicle has been added successfully!");
                }
            }
        }
        else
        {
            System.out.println("There are no available bays.");
        }
    }

    @Override
    public void printOptions() {
        System.out.println();
        System.out.println("[1] List all vehicles");
        System.out.println("[2] Add vehicle");
        System.out.println("[3] View vehicle detail");
        System.out.println("[4] Edit vehicle");
        System.out.println("[5] Move vehicle to bay");
        System.out.println("[6] Add seasonal record");
        System.out.println("[7] Delete vehicle");
        System.out.println("[0] Back");
    }

    public Vehicle listVehiclesAndSelect() {
        int number = 1;
        Vehicle vehicle = null;
        int choice = 0;

        List<Vehicle> vehicles = vehicleService.findAllVehiclesByUserId(SessionContext.getUser().getId());
        if (!vehicles.isEmpty()) {
            for (Vehicle v : vehicles) {
                System.out.println("[" + number + "] " +  v.getVehicleDisplay());
                number++;
            }
            System.out.println("Select a vehicle:");
            choice = InputHandler.getIntegerInput();
            vehicle = vehicles.get(choice - 1);
        }
        else
        {
            System.out.println("There are no vehicles available.");
        }

        return vehicle;
    }

    public Vehicle listVehiclesAndSelect(VehicleType vehicleType) {
        int number = 1;
        Vehicle vehicle = null;
        int choice = 0;

        List<Vehicle> vehicles = vehicleService.findAllVehiclesByUserId(SessionContext.getUser().getId()).stream().filter(v -> vehicleType.equals(v.getVehicleType())).toList();
        if (!vehicles.isEmpty()) {
            for (Vehicle v : vehicles) {
                System.out.println("[" + number + "] " +  v.getVehicleDisplay());
                number++;
            }
            System.out.println("Select a vehicle:");
            choice = InputHandler.getIntegerInput();
            vehicle = vehicles.get(choice - 1);
        }
        else
        {
            System.out.println("There are no vehicles available.");
        }

        return vehicle;
    }

}
