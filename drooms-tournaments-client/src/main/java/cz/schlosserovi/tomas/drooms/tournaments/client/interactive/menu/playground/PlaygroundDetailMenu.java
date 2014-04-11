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
        console.printLine("Current configuration:");
        console.printLine(singleLine());
        console.printLine("| Name:         | %-60s |", trimToSize(playground.getName(), 60));
        console.printLine("| Max players:  | %-60s |", playground.getMaxPlayers());
        console.printLine("|%s|", singleLine(78));
        console.printLine("| Configuration | %28s | %-29s |", "key", "value");
        console.printLine("|               |%s|", singleLine(62));
        for (String key : playground.getConfiguration().stringPropertyNames()) {
            console.print("|               |");
            console.print(" %28s |", trimToSize(key, 28));
            console.printLine(" %29s |", trimToSize(playground.getConfiguration().getProperty(key), 29));
        }
        console.printLine(singleLine());

        if (playground.getSource() != null) {
            console.printLine(playground.getSource());
        }
    }
}
