package io.auto.menu;

import io.auto.interfaces.IMenu;
import io.auto.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainMenu implements IMenu {

    @Autowired
    private AuthenticateMenu authenticateMenu;
    @Autowired
    private BayMenu bayMenu;
    @Autowired
    private MaintenanceMenu maintenanceMenu;
    @Autowired
    private ReportMenu reportMenu;
    @Autowired
    private VehicleMenu vehicleMenu;
    @Autowired
    private SettingsMenu settingsMenu;
    @Autowired
    private AlertMenu alertMenu;
    @Autowired
    private GoodbyeMenu goodbyeMenu;

    public void show() {
        int choice = 0;
        IMenu menu;

        System.out.println();
        displayTitle();
        authenticateMenu.automaticLogin();
        System.out.println();

        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            menu = switch (choice) {
                case 1 -> bayMenu;
                case 2 -> vehicleMenu;
                case 3 -> maintenanceMenu;
                case 4 -> reportMenu;
                case 5 -> alertMenu;
                case 6 -> settingsMenu;
                case 0 -> goodbyeMenu;
               default -> throw new IllegalStateException("Unexpected value: " + choice);
            };
            menu.show();
        }
        while (choice != 0);

        InputHandler.closeInput();
    }

    public void printOptions() {
        System.out.println("| MAIN MENU |");
        System.out.println("[1] Bays");
        System.out.println("[2] Vehicles");
        System.out.println("[3] Maintenance");
        System.out.println("[4] Reports");
        System.out.println("[5] Alerts");
        System.out.println("[6] Settings");
        System.out.println("[0] Exit");
        System.out.println("Please make a selection:");
    }

    public void displayTitle() {
        System.out.println("===========================================================");
        System.out.println("    Welcome to the Auto Warehouse Inventory Application!");
        System.out.println("============================================================");
    }

}
