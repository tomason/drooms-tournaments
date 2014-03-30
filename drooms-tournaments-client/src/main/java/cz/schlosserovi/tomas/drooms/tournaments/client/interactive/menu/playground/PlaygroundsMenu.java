package cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.playground;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.Choice;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.api.Menu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.util.OutputDevice;
import cz.schlosserovi.tomas.drooms.tournaments.client.services.TournamentsServerClient;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.services.PlaygroundService;

public class PlaygroundsMenu extends Menu {
    private List<Playground> playgrounds;
    private boolean ownedOnly;

    public PlaygroundsMenu(OutputDevice console, TournamentsServerClient client) {
        this(console, client, false);
        // by default show all the playgrounds available
    }

    public PlaygroundsMenu(OutputDevice console, TournamentsServerClient client, boolean ownedOnly) {
        super(console, client);
        this.ownedOnly = ownedOnly;
    }

    @Override
    protected String getHeadline() {
        return "playgrounds";
    }

    @Override
    protected Set<Choice> getChoices() {
        Set<Choice> choices = super.getChoices();

        // add generally available menus
        concatChoices(choices, Choice.PLAYGROUND_DETAIL);

        // add specific choices
        if (client.isLoggedIn()) {
            concatChoices(choices, Choice.PLAYGROUND_NEW, ownedOnly ? Choice.SHOW_ALL : Choice.SHOW_MINE);
        }

        return choices;
    }

    @Override
    protected void printInstructions() {
        playgrounds = loadPlaygrounds();

        console.print("List of all playgrounds:%n");
        console.print("%s%n", SINGLE_LINE);
        console.print("|   |                           name                                 | players |%n");
        console.print("%s%n", SINGLE_LINE);
        for (int i = 1; i <= playgrounds.size(); i++) {
            Playground p = playgrounds.get(i - 1);
            console.print("|%3s", i);
            console.print("|%63s ", trimToSize(p.getName(), 64));
            console.print("|%8s |%n", p.getMaxPlayers());
        }
        console.print("%s%n", SINGLE_LINE);
    }

    @Override
    protected Menu execute(Choice choice) {
        switch (choice) {
        case PLAYGROUND_NEW:
            return new NewPlaygroundMenu(console, client);
        case SHOW_ALL:
            return new PlaygroundsMenu(console, client, false);
        case SHOW_MINE:
            return new PlaygroundsMenu(console, client, true);
        case PLAYGROUND_DETAIL:
            int index = parseChoice("Playground index (see above table): ") - 1;
            if (index >= 0 && index < playgrounds.size()) {
                Playground p = playgrounds.get(index);
                return new PlaygroundDetailMenu(console, client, this, p);
            }
            break;
        default:
            return super.execute(choice);
        }

        return this;
    }

    private List<Playground> loadPlaygrounds() {
        List<Playground> playgrounds = new LinkedList<>();

        PlaygroundService service = client.getService(PlaygroundService.class);
        if (client.isLoggedIn() && ownedOnly) {
            playgrounds.addAll(service.getUserPlaygrounds());
        } else {
            playgrounds.addAll(service.getPlaygrounds());
        }

        return playgrounds;
    }
}
