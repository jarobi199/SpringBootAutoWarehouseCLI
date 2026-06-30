package io.auto.service;

import io.auto.authentication.SessionContext;
import io.auto.enums.BayType;
import io.auto.enums.ServiceType;
import io.auto.enums.VehicleType;
import io.auto.model.*;
import io.auto.repository.BayRepository;
import io.auto.repository.MaintenanceRepository;
import io.auto.repository.VehicleRepository;
import io.auto.util.BarChartUtil;
import io.auto.util.InputHandler;
import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private BayRepository bayRepository;
    @Autowired
    private MaintenanceRepository maintenanceRepository;

    public void fleetSummary() {
        int totalVehicles = vehicleRepository.findByUserId(SessionContext.getUser().getId()).size();
        int totalBays =  bayRepository.findByUserId(SessionContext.getUser().getId()).size();
        double totalFleetPurchaseValue = vehicleRepository.findByUserId(SessionContext.getUser().getId()).stream().mapToDouble(Vehicle::getPurchasePrice).sum();
        double totalFleetDepreciatedValue = vehicleRepository.findByUserId(SessionContext.getUser().getId()).stream().mapToDouble(Vehicle::calculateDepreciatedValue).sum();
        double occupancyRateDouble = (double) totalVehicles / totalBays;
        int occupancyRate = (int) (occupancyRateDouble * 100);

        FleetSummary fleetSummary = new FleetSummary(totalVehicles, totalBays, occupancyRate, totalFleetPurchaseValue, totalFleetDepreciatedValue);
        System.out.println("| FLEET SUMMARY |");
        Table fleetSummaryTable = Clique.table(TableType.BOX_DRAW)
                .headers(
                        "[*blue, bold]TOTAL VEHICLES[/]",
                        "[*blue, bold]TOTAL BAYS[/]",
                        "[*blue, bold]OCCUPANCY RATE[/]",
                        "[*blue, bold]TOTAL FLEET PURCHASE VALUE[/]",
                        "[*blue, bold]TOTAL DEPRECIATED FLEET VALUE[/]"
                )
        .row(String.valueOf(fleetSummary.totalVehicles()), String.valueOf(fleetSummary.totalBays()), fleetSummary.occupancyRate() + "%", InputHandler.formatAsMoney(fleetSummary.totalPurchaseValue()), InputHandler.formatAsMoney(fleetSummary.totalDepreciatedValue()));
        fleetSummaryTable.render();

        BarChartUtil.Builder barChart =  BarChartUtil.builder().title("TOTAL VALUE BY VEHICLE TYPE ");
        for (VehicleType vehicleType : VehicleType.values()) {
            double total = vehicleRepository.findByUserId(SessionContext.getUser().getId()).stream().filter(v -> v.getVehicleType().equals(vehicleType)).mapToDouble(Vehicle::getPurchasePrice).sum();
            barChart.bar(vehicleType.name(), total);
        }
        barChart.showTotal(true).render();
    }

    public void vehicleValuationReport() {
        List<Vehicle> vehicles = vehicleRepository.findByUserId(SessionContext.getUser().getId()).stream().sorted(Comparator.comparing(Vehicle::getPurchasePrice).reversed()).toList();

        System.out.println("| VEHICLE VALUATION REPORT |");
        Table valuationReportTable = Clique.table(TableType.BOX_DRAW)
                .headers(
                        "[*blue, bold]VEHICLE[/]",
                        "[*blue, bold]PURCHASE PRICE[/]",
                        "[*blue, bold]DEPRECIATED VALUE[/]",
                        "[*blue, bold]PERCENTAGE OF VALUE RETAINED[/]",
                        "[*blue, bold]YEARS OWNED[/]"
                );
        for (Vehicle vehicle : vehicles) {
            double percentageOfValueRetained = ((vehicle.getPurchasePrice() - vehicle.calculateDepreciatedValue()) / vehicle.getPurchasePrice()) * 100;
            valuationReportTable.row(vehicle.getVehicleDisplay(), InputHandler.formatAsMoney(vehicle.getPurchasePrice()), InputHandler.formatAsMoney(vehicle.calculateDepreciatedValue()), percentageOfValueRetained + "%",
                  String.valueOf(ChronoUnit.YEARS.between(vehicle.getPurchaseDate(), LocalDate.now())));
        }
        valuationReportTable.render();
    }

    public void costOfOwnership(Vehicle vehicle) {
        System.out.println("| COST OF OWNERSHIP REPORT |");
        Table costOfOwnershipTable = Clique.table(TableType.BOX_DRAW)
                .headers(
                        "[*blue, bold]VEHICLE[/]",
                        "[*blue, bold]PURCHASE PRICE[/]",
                        "[*blue, bold]TOTAL MAINTENANCE SPEND[/]",
                        "[*blue, bold]DEPRECIATED VALUE[/]",
                        "[*blue, bold]NET COST[/]"
                );

        double totalMaintenanceSpend = maintenanceRepository.findByVehicleId(vehicle.getId()).stream().mapToDouble(MaintenanceRecord::getCost).sum();
        double netCost = vehicle.getPurchasePrice() + totalMaintenanceSpend - vehicle.calculateDepreciatedValue();
        CostReport costReport = new CostReport(vehicle.getVehicleDisplay(), vehicle.getPurchasePrice(), totalMaintenanceSpend, vehicle.calculateDepreciatedValue(), netCost);

        costOfOwnershipTable.row(costReport.vehicleSummary(),  InputHandler.formatAsMoney(costReport.purchasePrice()),  InputHandler.formatAsMoney(costReport.totalMaintenanceSpend()), InputHandler.formatAsMoney(costReport.depreciatedValue()), InputHandler.formatAsMoney(costReport.netCostOfOwnership()));
        costOfOwnershipTable.render();
    }

    public void maintenanceCostByType() {
        List<String> vehicleIdList = vehicleRepository.findByUserId(SessionContext.getUser().getId()).stream().map(Vehicle::getId).toList();
        List<MaintenanceRecord> maintenanceRecords = maintenanceRepository.findByVehicleIdIn(vehicleIdList);
        Map<ServiceType,Double> maintenanceCostByTypeMap = maintenanceRecords.stream().collect(Collectors.groupingBy(
                MaintenanceRecord::getServiceType,
                Collectors.summingDouble(MaintenanceRecord::getCost)));

        BarChartUtil.Builder builder =  BarChartUtil.builder().title("MAINTENANCE COSTS BY TYPE");
        for (Map.Entry<ServiceType, Double> entry : maintenanceCostByTypeMap.entrySet()) {
            builder.bar(entry.getKey().name(), entry.getValue());
        }
        builder.showTotal(true).render();
    }

    public void bayOccupancyReport() {

        for(BayType bayType : BayType.values()) {
            List<Bay> bays =bayRepository.findByUserIdAndBayType(SessionContext.getUser().getId(), bayType);
            int totalBays = bays.size();
            int occupiedBays = bays.stream().filter(Bay::isOccupied).toList().size();
            int vacantBays = bays.stream().filter(bay -> !bay.isOccupied()).toList().size();
            double bayOccupancyRate = (double) occupiedBays / totalBays * 100;

            barChart.bar(bayType.name(), bayOccupancyRate);

            System.out.println("| " + bayType.name() + " BAY |");
            Table bayOccupancyTable = Clique.table(TableType.BOX_DRAW)
                    .headers(
                            "[*blue, bold]TOTAL BAYS[/]",
                            "[*blue, bold]OCCUPIED BAYS[/]",
                            "[*blue, bold]VACANT BAYS[/]"
                    );
            bayOccupancyTable.row(String.valueOf(totalBays), String.valueOf(occupiedBays), String.valueOf(vacantBays));
            bayOccupancyTable.render();
            System.out.println();
        }

        barChart.render();
    }

    private void printBar(String label, double value, double maxValue, int labelWidth) {
        int filled = (int) Math.round((value / maxValue) * maxWidth);
        int empty = maxWidth - filled;
        String bar = "█".repeat(filled) + "░".repeat(empty);
        String formattedLabel = String.format("%-" + labelWidth + "s", label);
        String formattedValue = String.format("$%,.2f", value);
        System.out.println(formattedLabel + "  " + bar + "  " + formattedValue);
    }
    //Occupancy count by bay type (STANDARD, OVERSIZED, etc.). Shows total bays, occupied bays, and vacant bays per type. Occupancy rate per type as a bar chart. Useful for facilities planning.
}
