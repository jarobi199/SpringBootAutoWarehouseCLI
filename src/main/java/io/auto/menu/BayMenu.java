package io.auto.menu;

import io.auto.authentication.SessionContext;
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
    }

    public void editBay() {
    }

    public void viewBayDetail() {
    }

    public void addBay() {
    }

    public void listVacantBays() {
    }

    public void listAllBays() {
    }

    @Override
    public void printOptions() {
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
        List<Bay> availableBays = bayService.findBaysByUserIdAndIsOccupied(SessionContext.getUser().getId(), false);
        for (Bay bay : availableBays) {
            System.out.println("[" + number + "] " + bay.getDisplay());
        }
        int choice = InputHandler.getIntegerInput();

        return availableBays.get(choice - 1);
    }

}
