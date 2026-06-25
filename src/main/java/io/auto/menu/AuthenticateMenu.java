package io.auto.menu;

import io.auto.enums.Role;
import io.auto.interfaces.IMenu;
import io.auto.service.AuthenticationService;
import io.auto.util.InputHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticateMenu implements IMenu {

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public void show() {
        boolean authenticated = false;

        while (!authenticated) {
            System.out.println("Please enter your username:");
            String username = InputHandler.getStringInput();
            System.out.println("Please enter your password:");
            String password = InputHandler.getStringInput();
            authenticated = authenticationService.authenticate(username, password);
            if (authenticated) {
                System.out.println("You have successfully logged in with the username: " + username + "!");
            } else {
                System.out.println("Invalid username or password!\n");
            }
        }
    }

    @Override
    public void printOptions() {
        //No options
    }

    public void initialize() {
        authenticationService.initializeUser("Kwame Jackson", "kjackson", "password", Role.ADMINISTRATOR, 500);
    }

    public void automaticLogin() {
        authenticationService.authenticate("kjackson", "password");
    }

}
