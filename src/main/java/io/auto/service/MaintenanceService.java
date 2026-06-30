package io.auto.service;

import io.auto.enums.ServiceType;
import io.auto.model.MaintenanceRecord;
import io.auto.model.Vehicle;
import io.auto.repository.MaintenanceRepository;
import io.auto.util.InputHandler;
import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class MaintenanceService {

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    public List<MaintenanceRecord> findByVehicleId(String vehicleId) {
        return maintenanceRepository.findByVehicleId(vehicleId);
    }

    public void addMaintenanceRecord(Vehicle vehicle, LocalDate serviceDate, ServiceType serviceType, String description, double cost, long mileage, String technician) {
    MaintenanceRecord maintenanceRecord = new MaintenanceRecord(vehicle.getId(), serviceDate, serviceType, description, cost, mileage, technician);
    maintenanceRepository.save(maintenanceRecord);
    }

    public void viewMaintenanceRecords(Vehicle vehicle) {
        System.out.println("| MAINTENANCE RECORDS |");
        List<MaintenanceRecord> maintenanceRecords = maintenanceRepository.findByVehicleIdOrderByServiceDateDesc(vehicle.getId());
        if (maintenanceRecords.isEmpty()) {
            System.out.println("No maintenance records found");
        }
        else
        {
            Table maintenanceTable = Clique.table(TableType.BOX_DRAW)
                    .headers(
                            "[*blue, bold]SERVICE DATE[/]",
                            "[*blue, bold]SERVICE TYPE[/]",
                            "[*blue, bold]DESCRIPTION[/]",
                            "[*blue, bold]COST[/]",
                            "[*blue, bold]MILEAGE[/]",
                            "[*blue, bold]TECHNICIAN[/]"
                    );
            maintenanceRecords.forEach(maintenanceRecord -> {
                maintenanceTable.row(maintenanceRecord.getServiceDate().toString(), maintenanceRecord.getServiceType().name(), maintenanceRecord.getDescription(),
                        InputHandler.formatAsMoney(maintenanceRecord.getCost()), String.valueOf(maintenanceRecord.getMileage()), maintenanceRecord.getTechnician());
            });
            maintenanceTable.render();

            double maintenanceTotal = maintenanceRecords.stream().mapToDouble(MaintenanceRecord::getCost).sum();
            System.out.println("TOTAL MAINTENANCE COST: " + InputHandler.formatAsMoney(maintenanceTotal));
        }
    }

    public void deleteMaintenanceRecord(MaintenanceRecord maintenanceRecord) {
        maintenanceRepository.delete(maintenanceRecord);
    }
}
