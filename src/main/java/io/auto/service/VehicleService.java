package io.auto.service;

import io.auto.authentication.SessionContext;
import io.auto.enums.FuelType;
import io.auto.enums.RvType;
import io.auto.enums.VehicleCondition;
import io.auto.enums.VehicleType;
import io.auto.model.*;
import io.auto.repository.BayRepository;
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
    private BayRepository bayRepository;

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

    public void addPassengerVehicle(VehicleType vehicleType, Bay bay, int year, String make, String model, String vin, double purchasePrice,
                                    LocalDate purchaseDate, LocalDate registrationExpiryDate, VehicleCondition condition, String notes, long odometerKm, FuelType fuelType, int seats, boolean isElectricOrHybrid) {
        PassengerVehicle passengerVehicle  = new PassengerVehicle(SessionContext.getUser().getId(), bay.getId(), make, model, year, vin, purchasePrice, purchaseDate, registrationExpiryDate, condition,
                                                                                                    notes, odometerKm, isElectricOrHybrid, seats, fuelType);
        vehicleRepository.save(passengerVehicle);
    }

    public void addMotorcycleVehicle(VehicleType vehicleType, Bay bay, int year, String make, String model, String vin, double purchasePrice, LocalDate purchaseDate, LocalDate registrationExpiryDate, VehicleCondition condition, String notes, int engineCC, boolean hasSideCar, long odometerKm) {
        MotorcycleVehicle motorcycleVehicle = new MotorcycleVehicle(SessionContext.getUser().getId(), bay.getId(), make, model, year, vin, purchasePrice, purchaseDate, registrationExpiryDate, condition,
                notes, engineCC, hasSideCar, odometerKm);
        vehicleRepository.save(motorcycleVehicle);
    }

    public void addRecreationalVehicle(VehicleType vehicleType, Bay bay, int year, String make, String model, String vin, double purchasePrice, LocalDate purchaseDate,
                                       LocalDate registrationExpiryDate, VehicleCondition condition, String notes, RvType rvType, String storageNotes, boolean requiresTrailer) {
        RecreationalVehicle recreationalVehicle = new RecreationalVehicle(SessionContext.getUser().getId(), bay.getId(), make, model, year, vin, purchasePrice, purchaseDate, registrationExpiryDate, condition,
                notes, requiresTrailer, storageNotes, rvType);
        vehicleRepository.save(recreationalVehicle);
    }
}
