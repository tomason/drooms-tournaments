package cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu;

import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.api.FormMenu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.api.Menu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.util.OutputDevice;
import cz.schlosserovi.tomas.drooms.tournaments.client.services.TournamentsServerClient;
import cz.schlosserovi.tomas.drooms.tournaments.domain.User;
import cz.schlosserovi.tomas.drooms.tournaments.services.UserService;

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
