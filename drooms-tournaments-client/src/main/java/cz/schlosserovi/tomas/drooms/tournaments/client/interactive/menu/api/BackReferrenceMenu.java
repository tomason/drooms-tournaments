package cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.api;

import java.util.Set;

import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.Choice;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.util.OutputDevice;
import cz.schlosserovi.tomas.drooms.tournaments.client.services.TournamentsServerClient;

/**
 * A menu that has added Back reference. Automatically adds BACK to choices and
 * performs the action defined in it.
 */
public abstract class BackReferrenceMenu extends Menu {
    private final Menu previous;

    protected BackReferrenceMenu(OutputDevice console, TournamentsServerClient client, Menu previous) {
        super(console, client);
        this.previous = previous;
    }

    @Override
    protected Set<Choice> getChoices() {
        return concatChoices(super.getChoices(), Choice.BACK);
    }

    @Override
    protected Menu execute(Choice choice) {
        if (choice == Choice.BACK) {
            return back();
        } else {
            return super.execute(choice);
        }
    }

    private Menu back() {
        return previous;
    }
}
