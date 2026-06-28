package io.auto.service;

import io.auto.authentication.SessionContext;
import io.auto.enums.BayType;
import io.auto.model.Bay;
import io.auto.model.Vehicle;
import io.auto.repository.BayRepository;
import io.auto.repository.VehicleRepository;
import io.auto.util.InputHandler;
import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.components.Table;
import io.github.kusoroadeolu.clique.configuration.TableType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BayService {
    @Autowired
    private BayRepository bayRepository;
    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Bay> findBaysByUserId(String userId) {
        return bayRepository.findByUserId(userId);
    }

    public List<Bay> findBaysByUserIdAndIsOccupied(String userId, boolean isOccupied) {
        return bayRepository.findByUserIdAndIsOccupied(userId, isOccupied);
    }

    public void listAllBays() {
        List<Bay> bays = bayRepository.findByUserId(SessionContext.getUser().getId());
        displayBays(bays);
    }

    public void listAllVacantBays() {
        List<Bay> bays = bayRepository.findByUserIdAndIsOccupied(SessionContext.getUser().getId(), false);
        displayBays(bays);
    }

    public void displayBays(List<Bay> bays) {
        Table table = Clique.table(TableType.BOX_DRAW)
                .headers(
                        "[*blue, bold]NUMBER[/]",
                        "[*blue, bold]NAME[/]",
                        "[*blue, bold]TYPE[/]",
                        "[*blue, bold]OCCUPANCY STATUS[/]",
                        "[*blue, bold]NOTES[/]"
                );
        bays.forEach(bay -> table.row(String.valueOf(bay.getBayNumber()), bay.getName(), bay.getBayType().name(), bay.isOccupied() ? "OCCUPIED" : "VACANT", bay.getNotes()));
        System.out.println("| BAYS |");
        table.render();
    }

    public void addBay(int bayNumber, String name, BayType bayType, String notes) {
        Bay bay = new Bay(SessionContext.getUser().getId(), bayNumber, name, bayType, false, notes);
        bayRepository.save(bay);
    }

    public void updateBay(String name, BayType bayType, String notes, Bay bay) {
        bay.setName(name);
        bay.setBayType(bayType);
        bay.setNotes(notes);
        bayRepository.save(bay);
    }

    public void deleteBay(Bay bay) {
        bayRepository.delete(bay);
    }

    public void viewBayDetail(Bay bay) {
        displayBays(List.of(bay));
        System.out.println();
        System.out.println("| VEHICLE SUMMARY |");
        if(bay.isOccupied()) {
            Vehicle vehicle = vehicleRepository.findByBayId(bay.getId());
            Table table = Clique.table(TableType.BOX_DRAW)
                    .headers(
                            "[*blue, bold]YEAR[/]",
                            "[*blue, bold]MAKE[/]",
                            "[*blue, bold]MODEL[/]",
                            "[*blue, bold]CONDITION[/]",
                            "[*blue, bold]PURCHASE PRICE[/]",
                            "[*blue, bold]DEPRECIATED VALUE[/]"
                    )
                    .row(String.valueOf(vehicle.getYear()), vehicle.getMake(), vehicle.getModel(), vehicle.getCondition().name(), InputHandler.formatAsMoney(vehicle.getPurchasePrice()), InputHandler.formatAsMoney(vehicle.calculateDepreciatedValue()));
            table.render();
        }
        else
        {
            System.out.println("This bay is unoccupied and does not have any vehicles.");
        }
    }

}
