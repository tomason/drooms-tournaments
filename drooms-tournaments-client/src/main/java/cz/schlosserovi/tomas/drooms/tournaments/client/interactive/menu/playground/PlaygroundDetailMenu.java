package cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.playground;

import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.api.BackReferrenceMenu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.api.Menu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.util.OutputDevice;
import cz.schlosserovi.tomas.drooms.tournaments.client.services.TournamentsServerClient;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;

class PlaygroundDetailMenu extends BackReferrenceMenu {
    private Playground playground;

    protected PlaygroundDetailMenu(OutputDevice console, TournamentsServerClient client, Menu previous, Playground playground) {
        super(console, client, previous);
        this.playground = playground;
    }

    @Override
    protected String getHeadline() {
        return playground.getName() + " detail";
    }

    @Override
    protected void printInstructions() {
        printPlaygroundDetail(console, playground);
    }

    static void printPlaygroundDetail(OutputDevice console, Playground playground) {
        console.print("Current configuration:%n");
        console.print("%s%n", SINGLE_LINE);
        console.print("| Name:         | %-60s |%n", trimToSize(playground.getName(), 60));
        console.print("| Max players:  | %-60s |%n", playground.getMaxPlayers());
        console.print("|------------------------------------------------------------------------------|%n");
        console.print("| Configuration | %28s | %-29s |%n", "key", "value");
        console.print("|               |--------------------------------------------------------------|%n");
        for (String key : playground.getConfiguration().stringPropertyNames()) {
            console.print("|               |");
            console.print(" %28s |", trimToSize(key, 28));
            console.print(" %29s |%n", trimToSize(playground.getConfiguration().getProperty(key), 29));
        }
        console.print("%s%n", SINGLE_LINE);
    }
}
