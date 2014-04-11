package org.drooms.tournaments.client.interactive.menu.playground;

import java.util.Set;

import org.drooms.tournaments.client.interactive.menu.Choice;
import org.drooms.tournaments.client.interactive.menu.api.BackReferrenceMenu;
import org.drooms.tournaments.client.interactive.menu.api.Menu;
import org.drooms.tournaments.client.interactive.util.OutputDevice;
import org.drooms.tournaments.client.services.TournamentsServerClient;
import org.drooms.tournaments.domain.Playground;
import org.drooms.tournaments.services.PlaygroundService;

public class PlaygroundConfirmationMenu extends BackReferrenceMenu {
    private Playground playground;

    protected PlaygroundConfirmationMenu(OutputDevice console, TournamentsServerClient client, Menu previous,
            Playground playground) {
        super(console, client, previous);
        this.playground = playground;
    }

    @Override
    protected String getHeadline() {
        return playground.getName() + " confirmation";
    }

    @Override
    protected Set<Choice> getChoices() {
        return concatChoices(super.getChoices(), Choice.PLAYGROUND_CREATE, Choice.PLAYGROUND_DISCARD);
    }

    @Override
    protected void printInstructions() {
        PlaygroundDetailMenu.printPlaygroundDetail(console, playground);
        console.printLine("%s", playground.getSource());
    }

    @Override
    protected Menu execute(Choice choice) {
        switch (choice) {
        case PLAYGROUND_CREATE:
            client.getService(PlaygroundService.class).newPlayground(playground);
        case PLAYGROUND_DISCARD:
            return new PlaygroundsMenu(console, client);
        default:
            return super.execute(choice);
        }
    }

}
