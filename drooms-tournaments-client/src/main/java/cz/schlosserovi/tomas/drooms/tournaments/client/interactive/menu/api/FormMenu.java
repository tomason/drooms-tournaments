package cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.api;

import java.util.Set;

import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.Choice;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.util.OutputDevice;
import cz.schlosserovi.tomas.drooms.tournaments.client.services.TournamentsServerClient;

/**
 * A menu that is only intended for user's input. It does not have any choices
 * printed and should expect to receive <code>null</code> as the
 * <code>execute</code> method parameter.
 */
public abstract class FormMenu extends Menu {

    protected FormMenu(OutputDevice console, TournamentsServerClient client) {
        super(console, client);
    }

    @Override
    protected Set<Choice> getChoices() {
        return null;
    }

}
