package org.drooms.tournaments.client.interactive.menu.playground;

import java.util.Map.Entry;

import org.drooms.tournaments.client.interactive.menu.api.BackReferrenceMenu;
import org.drooms.tournaments.client.interactive.menu.api.Menu;
import org.drooms.tournaments.client.interactive.util.OutputDevice;
import org.drooms.tournaments.client.services.TournamentsServerClient;
import org.drooms.tournaments.domain.Playground;

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
        for (Entry<String, String> entry : playground.getConfiguration().entrySet()) {
            console.print("|               |");
            console.print(" %28s |", trimToSize(entry.getKey(), 28));
            console.printLine(" %29s |", trimToSize(entry.getValue(), 29));
        }
        console.printLine(singleLine());

        if (playground.getSource() != null) {
            console.printLine(playground.getSource());
        }
    }
}
