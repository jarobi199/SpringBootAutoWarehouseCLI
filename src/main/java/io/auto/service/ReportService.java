package io.auto.service;

import io.auto.authentication.SessionContext;
import io.auto.enums.VehicleType;
import io.auto.model.CostReport;
import io.auto.model.FleetSummary;
import io.auto.model.MaintenanceRecord;
import io.auto.model.Vehicle;
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

    }

  /*
    class Item {
        private String name;
        private int quantity;

        public Item(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        public String getName() { return name; }
        public int getQuantity() { return quantity; }
    }

    public class Main {
        public static void main(String[] args) {
            // 1. Create a list of items with duplicate names
            List<Item> items = Arrays.asList(
                    new Item("Apple", 10),
                    new Item("Banana", 20),
                    new Item("Apple", 15),
                    new Item("Orange", 5),
                    new Item("Banana", 5)
            );

            // 2. Group by item name and sum the quantities
            Map<String, Integer> totalByProduct = items.stream()
                    .collect(Collectors.groupingBy(
                            Item::getName,                             // Key extractor (Group By)
                            Collectors.summingInt(Item::getQuantity)   // Downstream collector (Sum)
                    ));

            // 3. Print the resulting Map
            System.out.println(totalByProduct);
        }
    }*/

}
