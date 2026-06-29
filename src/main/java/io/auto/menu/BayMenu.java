package io.auto.menu;

import io.auto.authentication.SessionContext;
import io.auto.enums.BayType;
import io.auto.interfaces.IMenu;
import io.auto.model.Bay;
import io.auto.service.BayService;
import io.auto.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BayMenu implements IMenu {

    @Autowired
    private BayService bayService;

    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> listAllBays();
                case 2 -> listVacantBays();
                case 3 -> addBay();
                case 4 -> viewBayDetail();
                case 5 -> editBay();
                case 6 -> deleteBay();
            }
        }
        while (choice != 0);
    }

    public void deleteBay() {
        Bay bay = listBaysAndSelect();
        System.out.println("Are you sure you want to delete this bay? (Y/N):");

        if ((bay != null)  && (!bay.isOccupied())) {
            String answer = InputHandler.getStringInput();
            if ("Y".equalsIgnoreCase(answer)) {
                bayService.deleteBay(bay);
                System.out.println("Bay has been deleted!");
            }
        }
        else
        {
            System.out.println("There are no bays created or the selected bay is occupied.");
        }
    }

    public void editBay() {
        Bay bay = listBaysAndSelect();
        if (bay != null) {
            System.out.println("Enter the new bay name");
            String name = InputHandler.getStringInput();
            System.out.println("Enter the new bay type (STANDARD, OVERSIZED, CLIMATE_CONTROLLED, SECURE_VAULT, OUTDOOR_COVERED, OTHER):");
            BayType bayType = BayType.valueOf(InputHandler.getStringInput().toUpperCase());
            System.out.println("Enter the new bay notes:");
            String notes = InputHandler.getStringInput();

            bayService.updateBay(name, bayType, notes, bay);
            System.out.println("Bay has been successfully updated!");
        }
    }

    public void viewBayDetail() {
        Bay bay = listBaysAndSelect();
        if (bay != null) {
            bayService.viewBayDetail(bay);
        }
    }

    public void addBay() {
        System.out.println("Enter the bay number:");
        int bayNumber = InputHandler.getIntegerInput();
        System.out.println("Enter the name:");
        String name = InputHandler.getStringInput();
        System.out.println("Enter the bay type (STANDARD, OVERSIZED, CLIMATE_CONTROLLED, SECURE_VAULT, OUTDOOR_COVERED, OTHER):");
        BayType bayType = BayType.valueOf(InputHandler.getStringInput().toUpperCase());
        System.out.println("Enter the notes:");
        String notes = InputHandler.getStringInput();

        bayService.addBay(bayNumber, name, bayType, notes);
        System.out.println("Bay has been added!");
    }

    public void listVacantBays() {
        bayService.listAllVacantBays();
    }

    public void listAllBays() {
        bayService.listAllBays();
    }

    @Override
    public void printOptions() {
        System.out.println();
        System.out.println("| BAY MENU |");
        System.out.println("[1] List all bays");
        System.out.println("[2] List vacant bays");
        System.out.println("[3] Add bay");
        System.out.println("[4] View bay detail");
        System.out.println("[5] Edit bay");
        System.out.println("[6] Delete bay");
        System.out.println("[0] Back");
    }

    public Bay listBaysAndSelect() {
        int number = 1;
        Bay bay = null;
        int choice = 0;

        List<Bay> availableBays = bayService.findBaysByUserIdAndIsOccupied(SessionContext.getUser().getId(), false);
        if (!availableBays.isEmpty()) {
            for (Bay b : availableBays) {
                System.out.println("[" + number + "] " + b.getDisplay());
                number++;
            }
            System.out.println("Select a bay:");
            choice = InputHandler.getIntegerInput();
            bay = availableBays.get(choice - 1);
        }

        return bay;
    }

}
