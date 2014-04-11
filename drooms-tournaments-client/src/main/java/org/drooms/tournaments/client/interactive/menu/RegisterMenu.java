package org.drooms.tournaments.client.interactive.menu;

import org.drooms.tournaments.client.interactive.menu.api.FormMenu;
import org.drooms.tournaments.client.interactive.menu.api.Menu;
import org.drooms.tournaments.client.interactive.util.OutputDevice;
import org.drooms.tournaments.client.services.TournamentsServerClient;
import org.drooms.tournaments.domain.User;
import org.drooms.tournaments.services.UserService;

class RegisterMenu extends FormMenu {

    public RegisterMenu(OutputDevice console, TournamentsServerClient client) {
        super(console, client);
    }

    @Override
    protected String getHeadline() {
        return "registration";
    }

    @Override
    protected Menu execute(Choice choice) {
        String username = console.readLine("User name: ");
        String password = console.readPassword("Password: ");

        client.getService(UserService.class).register(new User(username, password));
        client.login(username, password);

        return new MainMenu(console, client);
    }

}
