package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

import java.io.Console;

import cz.schlosserovi.tomas.drooms.tournaments.client.TournamentsServerClient;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;

public class NewPlaygroundMenu extends FormMenu {

    protected NewPlaygroundMenu(Console console, TournamentsServerClient client) {
        super(console, client);
    }

    @Override
    protected String getHeadline() {
        return ("new playground");
    }

    @Override
    protected Menu execute(int choice) {
        // TODO information about new playgrounds
        String name = console.readLine("New playground name: ");
        console.format("New layout (hit Ctrl+D to send EOF to stop reading:%n");
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = console.readLine()) != null) {
            sb.append(line).append("\n");
        }

        Playground playground = new Playground(name, sb.toString());
        client.newPlayground(playground);

        return new ConfigurePlaygroundMenu(console, client, playground);
    }

}
