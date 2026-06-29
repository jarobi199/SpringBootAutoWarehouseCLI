package io.auto.service;

import io.auto.authentication.SessionContext;
import io.auto.enums.FuelType;
import io.auto.enums.RvType;
import io.auto.enums.VehicleCondition;
import io.auto.enums.VehicleType;
import io.auto.model.*;
import io.auto.repository.BayRepository;
import io.auto.repository.MaintenanceRepository;
import io.auto.repository.VehicleRepository;
import io.auto.util.InputHandler;
import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private MaintenanceRepository maintenanceRepository;
    @Autowired
    private BayRepository bayRepository;

    public List<Vehicle> findAllVehiclesByUserId(String userId) {
        return vehicleRepository.findByUserId(userId);
    }

    public void listAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findByUserId(SessionContext.getUser().getId());
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles found");
        }
        else {
            System.out.println("| VEHICLES |");
            Table table = Clique.table(TableType.BOX_DRAW)
                    .headers(
                            "[*blue, bold]YEAR[/]",
                            "[*blue, bold]MAKE[/]",
                            "[*blue, bold]MODEL[/]",
                            "[*blue, bold]TYPE[/]",
                            "[*blue, bold]BAY[/]",
                            "[*blue, bold]CONDITION[/]",
                            "[*blue, bold]PURCHASE PRICE[/]",
                            "[*blue, bold]DEPRECIATED VALUE[/]"
                    );
            vehicles.forEach(vehicle -> {
                Optional<Bay> optionalBay = bayRepository.findById(vehicle.getBayId());
                table.row(String.valueOf(vehicle.getYear()), vehicle.getMake(), vehicle.getModel(), vehicle.getVehicleType().name(),
                        (optionalBay.isPresent()) ? optionalBay.get().getName() : "", vehicle.getCondition().name(), InputHandler.formatAsMoney(vehicle.getPurchasePrice()), InputHandler.formatAsMoney(vehicle.calculateDepreciatedValue()));
            });
            table.render();
        }
    }

    public void addPassengerVehicle(Bay bay, int year, String make, String model, String vin, double purchasePrice,
                                    LocalDate purchaseDate, LocalDate registrationExpiryDate, VehicleCondition condition, String notes, long odometerKm, FuelType fuelType, int seats, boolean isElectricOrHybrid) {
        PassengerVehicle passengerVehicle  = new PassengerVehicle(SessionContext.getUser().getId(), bay.getId(), make, model, year, vin, purchasePrice, purchaseDate, registrationExpiryDate, condition,
                                                                                                    notes, odometerKm, isElectricOrHybrid, seats, fuelType);

        vehicleRepository.save(passengerVehicle);
        bay.setOccupied(true);
        bayRepository.save(bay);
    }

    public void addMotorcycleVehicle(Bay bay, int year, String make, String model, String vin, double purchasePrice, LocalDate purchaseDate, LocalDate registrationExpiryDate, VehicleCondition condition, String notes, int engineCC, boolean hasSideCar, long odometerKm) {
        MotorcycleVehicle motorcycleVehicle = new MotorcycleVehicle(SessionContext.getUser().getId(), bay.getId(), make, model, year, vin, purchasePrice, purchaseDate, registrationExpiryDate, condition,
                notes, engineCC, hasSideCar, odometerKm);

        vehicleRepository.save(motorcycleVehicle);
        bay.setOccupied(true);
        bayRepository.save(bay);
    }

    public void addRecreationalVehicle(Bay bay, int year, String make, String model, String vin, double purchasePrice, LocalDate purchaseDate,
                                       LocalDate registrationExpiryDate, VehicleCondition condition, String notes, RvType rvType, String storageNotes, boolean requiresTrailer) {
        RecreationalVehicle recreationalVehicle = new RecreationalVehicle(SessionContext.getUser().getId(), bay.getId(), make, model, year, vin, purchasePrice, purchaseDate, registrationExpiryDate, condition,
                notes, requiresTrailer, storageNotes, rvType);

        vehicleRepository.save(recreationalVehicle);
        bay.setOccupied(true);
        bayRepository.save(bay);
    }

    public void moveVehicle(Vehicle vehicle, Bay newBay) {
        Optional<Bay> optionalOldBay = bayRepository.findById(vehicle.getBayId());
        optionalOldBay.ifPresent(oldBay ->  {
            oldBay.setOccupied(false);
            newBay.setOccupied(true);
            vehicle.setBayId(newBay.getId());

            bayRepository.save(oldBay);
            bayRepository.save(newBay);
            vehicleRepository.save(vehicle);
        });
    }


    public void addSeasonalRecord(RecreationalVehicle recreationalVehicle, String season, int hoursUsed, int bayLocation, String notes) {
        SeasonalRecord seasonalRecord = new SeasonalRecord(season, hoursUsed, bayLocation, notes);
        recreationalVehicle.getUsageLogs().add(seasonalRecord);

        vehicleRepository.save(recreationalVehicle);
    }

    public void viewDetails(Vehicle vehicle) {
        Optional<Bay> optionalBay = bayRepository.findById(vehicle.getBayId());
        if(VehicleType.RECREATIONAL.equals(vehicle.getVehicleType())) {
            RecreationalVehicle recreationalVehicle = (RecreationalVehicle) vehicle;

            System.out.println("| RECREATIONAL VEHICLE |");
            Table table = Clique.table(TableType.BOX_DRAW)
                    .headers(
                            "[*blue, bold]VIN[/]",
                            "[*blue, bold]YEAR[/]",
                            "[*blue, bold]MAKE[/]",
                            "[*blue, bold]MODEL[/]",
                            "[*blue, bold]TYPE[/]",
                            "[*blue, bold]BAY[/]",
                            "[*blue, bold]CONDITION[/]",
                            "[*blue, bold]PURCHASE PRICE[/]",
                            "[*blue, bold]PURCHASE DATE[/]",
                            "[*blue, bold]REGISTRATION EXPIRY DATE[/]",
                            "[*blue, bold]DEPRECIATED VALUE[/]",
                            "[*blue, bold]NOTES[/]",
                            "[*blue, bold]RV TYPE[/]",
                            "[*blue, bold]STORAGE NOTES[/]",
                            "[*blue, bold]REQUIRES TRAILER[/]"
                    );
            table.row(recreationalVehicle.getVin(), String.valueOf(recreationalVehicle.getYear()), recreationalVehicle.getMake(), recreationalVehicle.getModel(), recreationalVehicle.getVehicleType().name(),
                    (optionalBay.isPresent()) ? optionalBay.get().getName() : "", recreationalVehicle.getCondition().name(), InputHandler.formatAsMoney(recreationalVehicle.getPurchasePrice()),
                    recreationalVehicle.getPurchaseDate().toString(), recreationalVehicle.getRegistrationExpiryDate().toString(), InputHandler.formatAsMoney(recreationalVehicle.calculateDepreciatedValue()),
                    recreationalVehicle.getNotes(), recreationalVehicle.getRvType().name(), recreationalVehicle.getStorageNotes(), recreationalVehicle.isRequiresTrailer() ? "YES" : "NO");
            table.render();

            System.out.println();
            System.out.println("| SEASONAL LOGS |");
            List<SeasonalRecord> usageLogs = recreationalVehicle.getUsageLogs();
            if(!usageLogs.isEmpty()) {
                Table seasonalTable = Clique.table(TableType.BOX_DRAW)
                        .headers(
                                "[*blue, bold]SEASON[/]",
                                "[*blue, bold]HOURS USED[/]",
                                "[*blue, bold]BAY NUMBER[/]",
                                "[*blue, bold]NOTES[/]");
                usageLogs.forEach(seasonalRecord -> seasonalTable.row(seasonalRecord.season(), String.valueOf(seasonalRecord.hoursUsed()), String.valueOf(seasonalRecord.bayLocation()), seasonalRecord.notes()));
                seasonalTable.render();
            }
            else
            {
                System.out.println("There are no seasonal records for this vehicle.");
            }
        }
        else if(VehicleType.PASSENGER.equals(vehicle.getVehicleType())) {
            PassengerVehicle passengerVehicle = (PassengerVehicle) vehicle;

            System.out.println("| PASSENGER VEHICLE |");
            Table table = Clique.table(TableType.BOX_DRAW)
                    .headers(
                            "[*blue, bold]VIN[/]",
                            "[*blue, bold]YEAR[/]",
                            "[*blue, bold]MAKE[/]",
                            "[*blue, bold]MODEL[/]",
                            "[*blue, bold]TYPE[/]",
                            "[*blue, bold]BAY[/]",
                            "[*blue, bold]CONDITION[/]",
                            "[*blue, bold]PURCHASE PRICE[/]",
                            "[*blue, bold]PURCHASE DATE[/]",
                            "[*blue, bold]REGISTRATION EXPIRY DATE[/]",
                            "[*blue, bold]DEPRECIATED VALUE[/]",
                            "[*blue, bold]NOTES[/]",
                            "[*blue, bold]ODOMETER[/]",
                            "[*blue, bold]FUEL TYPE[/]",
                            "[*blue, bold]NUMBER OF SEATS[/]"
                    );
            table.row(passengerVehicle.getVin(), String.valueOf(passengerVehicle.getYear()), passengerVehicle.getMake(), passengerVehicle.getModel(), passengerVehicle.getVehicleType().name(),
                    (optionalBay.isPresent()) ? optionalBay.get().getName() : "", passengerVehicle.getCondition().name(), InputHandler.formatAsMoney(passengerVehicle.getPurchasePrice()),
                    passengerVehicle.getPurchaseDate().toString(), passengerVehicle.getRegistrationExpiryDate().toString(), InputHandler.formatAsMoney(passengerVehicle.calculateDepreciatedValue()),
                    passengerVehicle.getNotes(), String.valueOf(passengerVehicle.getOdometerKm()), passengerVehicle.getFuelType().name(), String.valueOf(passengerVehicle.getNumberOfSeats()));
            table.render();
        }
        else if(VehicleType.MOTORCYCLE.equals(vehicle.getVehicleType())) {
            MotorcycleVehicle motorcycleVehicle = (MotorcycleVehicle) vehicle;

            System.out.println("| MOTORCYCLE VEHICLE |");
            Table table = Clique.table(TableType.BOX_DRAW)
                    .headers(
                            "[*blue, bold]VIN[/]",
                            "[*blue, bold]YEAR[/]",
                            "[*blue, bold]MAKE[/]",
                            "[*blue, bold]MODEL[/]",
                            "[*blue, bold]TYPE[/]",
                            "[*blue, bold]BAY[/]",
                            "[*blue, bold]CONDITION[/]",
                            "[*blue, bold]PURCHASE PRICE[/]",
                            "[*blue, bold]PURCHASE DATE[/]",
                            "[*blue, bold]REGISTRATION EXPIRY DATE[/]",
                            "[*blue, bold]DEPRECIATED VALUE[/]",
                            "[*blue, bold]NOTES[/]",
                            "[*blue, bold]ENGINE CC[/]",
                            "[*blue, bold]SIDE CAR[/]",
                            "[*blue, bold]ODOMETER[/]"
                    );
            table.row(motorcycleVehicle.getVin(), String.valueOf(motorcycleVehicle.getYear()), motorcycleVehicle.getMake(), motorcycleVehicle.getModel(), motorcycleVehicle.getVehicleType().name(),
                    (optionalBay.isPresent()) ? optionalBay.get().getName() : "", motorcycleVehicle.getCondition().name(), InputHandler.formatAsMoney(motorcycleVehicle.getPurchasePrice()),
                    motorcycleVehicle.getPurchaseDate().toString(), motorcycleVehicle.getRegistrationExpiryDate().toString(), InputHandler.formatAsMoney(motorcycleVehicle.calculateDepreciatedValue()),
                    motorcycleVehicle.getNotes(), String.valueOf(motorcycleVehicle.getEngineCC()), motorcycleVehicle.isHasSideCar() ? "YES" : "NO", String.valueOf(motorcycleVehicle.getOdometerKm()));
            table.render();
        }

    }

    public void deleteVehicle(Vehicle vehicle) {
        List<MaintenanceRecord> maintenanceRecords = maintenanceRepository.findByVehicleId(vehicle.getId());
        maintenanceRecords.forEach(maintenanceRecord -> {
            maintenanceRepository.delete(maintenanceRecord);
        });

        Optional<Bay> optionalBay = bayRepository.findById(vehicle.getBayId());
        optionalBay.ifPresent(bay -> {
            bay.setOccupied(false);
            bayRepository.save(bay);
        });

        vehicleRepository.delete(vehicle);
    }

    public void editVehicle(Vehicle vehicle, LocalDate newRegistrationExpiryDate, VehicleCondition newCondition, String newNotes, String newStorageNotes, int newEngineCC, long newOdometerKm, FuelType newFuelType) {
        vehicle.setRegistrationExpiryDate(newRegistrationExpiryDate);
        vehicle.setCondition(newCondition);
        vehicle.setNotes(newNotes);
        if(vehicle.getVehicleType().equals(VehicleType.RECREATIONAL)) {
            RecreationalVehicle recreationalVehicle = (RecreationalVehicle) vehicle;
            recreationalVehicle.setStorageNotes(newStorageNotes);
            vehicleRepository.save(recreationalVehicle);
        }
        else if(vehicle.getVehicleType().equals(VehicleType.MOTORCYCLE)) {
            MotorcycleVehicle motorcycleVehicle = (MotorcycleVehicle) vehicle;
            motorcycleVehicle.setEngineCC(newEngineCC);
            vehicleRepository.save(motorcycleVehicle);
        }
        else if (vehicle.getVehicleType().equals(VehicleType.PASSENGER)) {
            PassengerVehicle passengerVehicle = (PassengerVehicle) vehicle;
            passengerVehicle.setFuelType(newFuelType);
            passengerVehicle.setOdometerKm(newOdometerKm);
            vehicleRepository.save(passengerVehicle);
        }
    }
}
