package org.drooms.tournaments.client.interactive.menu;

import org.drooms.tournaments.client.interactive.menu.api.FormMenu;
import org.drooms.tournaments.client.interactive.menu.api.Menu;
import org.drooms.tournaments.client.interactive.util.OutputDevice;
import org.drooms.tournaments.client.services.TournamentsServerClient;
import org.drooms.tournaments.services.StrategyService;

public class LoginMenu extends FormMenu {

    public LoginMenu(OutputDevice console, TournamentsServerClient client) {
        super(console, client);
    }

    @Override
    protected String getHeadline() {
        return "login";
    }

    @Override
    protected Menu execute(Choice choice) {
        String username = console.readLine("User name: ");
        String password = console.readPassword("Password: ");

        try {
            client.login(username, password);
            client.getService(StrategyService.class).getUserStrategies(); // make sure authentication works
        } catch (Exception ex) {
            console.printLine("Unable to login %s, check your username and password", username);
            client.logout(); // sets the credentials to null to not show user as logged in
        }

        return new MainMenu(console, client);
    }

}
