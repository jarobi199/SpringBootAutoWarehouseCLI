package io.auto.menu;

import io.auto.interfaces.IMenu;
import io.auto.util.InputHandler;
import org.springframework.stereotype.Component;

@Component
public class VehicleMenu implements IMenu {
    @Override
    public void show() {
        int choice;
        do {
            printOptions();
            choice = InputHandler.getIntegerInput();
            switch (choice) {
                case 1 -> listAllVehicles();
                case 2 -> addVehicle();
                case 3 -> viewVehicleDetail();
                case 4 -> editVehicle();
                case 5 -> moveVehicleToBay();
            }
        }
        while (choice != 0);
    }

    public void moveVehicleToBay() {
    }

    public void editVehicle() {
    }

    public void viewVehicleDetail() {
    }

    public void listAllVehicles() {
    }

    public void addVehicle() {
    }

    @Override
    public void printOptions() {
        System.out.println("[1] List all vehicles");
        System.out.println("[2] Add vehicle");
        System.out.println("[2] View vehicle detail");
        System.out.println("[2] Edit vehicle");
        System.out.println("[2] Move vehicle to bay");
    }
}
