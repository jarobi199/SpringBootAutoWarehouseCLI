package io.auto.service;

import io.auto.authentication.SessionContext;
import io.auto.enums.VehicleType;
import io.auto.model.FleetSummary;
import io.auto.model.Vehicle;
import io.auto.repository.BayRepository;
import io.auto.repository.VehicleRepository;
import io.auto.util.BarChartUtil;
import io.auto.util.InputHandler;
import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private BayRepository bayRepository;

    public void fleetSummary() {
        int totalVehicles = vehicleRepository.findByUserId(SessionContext.getUser().getId()).size();
        int totalBays =  bayRepository.findByUserId(SessionContext.getUser().getId()).size();
        double totalFleetPurchaseValue = vehicleRepository.findByUserId(SessionContext.getUser().getId()).stream().mapToDouble(Vehicle::getPurchasePrice).sum();
        double totalFleetDepreciatedValue = vehicleRepository.findByUserId(SessionContext.getUser().getId()).stream().mapToDouble(Vehicle::calculateDepreciatedValue).sum();
        int occupancyRate = (totalVehicles / totalBays) * 100;

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
}
