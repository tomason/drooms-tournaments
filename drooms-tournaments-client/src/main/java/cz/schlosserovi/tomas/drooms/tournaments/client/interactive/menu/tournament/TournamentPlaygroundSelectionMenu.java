package cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.tournament;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.Choice;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.api.Menu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.util.OutputDevice;
import cz.schlosserovi.tomas.drooms.tournaments.client.services.TournamentsServerClient;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Tournament;
import cz.schlosserovi.tomas.drooms.tournaments.services.PlaygroundService;

class TournamentPlaygroundSelectionMenu extends Menu {
    private final Tournament tournament;
    private final List<PlaygroundSelection> playgrounds = new LinkedList<>();

    protected TournamentPlaygroundSelectionMenu(OutputDevice console, TournamentsServerClient client, Tournament tournament) {
        super(console, client);
        this.tournament = tournament;
        tournament.setPlaygrounds(new LinkedList<Playground>());
        for (Playground playground : client.getService(PlaygroundService.class).getPlaygrounds()) {
            playgrounds.add(new PlaygroundSelection(playground));
        }
    }

    @Override
    protected String getHeadline() {
        return "new tournament";
    }

    @Override
    protected Set<Choice> getChoices() {
        return concatChoices(super.getChoices(), Choice.PLAYGROUND_ADD, Choice.PLAYGROUND_REMOVE, Choice.DONE);
    }

    @Override
    protected void printInstructions() {
        console.print("%s%n", SINGLE_LINE);
        console.print("|%3s|%3s| %54s | %11s |%n", "", "", "name", "max players");
        console.print("%s%n", SINGLE_LINE);
        for (int i = 1; i <= playgrounds.size(); i++) {
            PlaygroundSelection selection = playgrounds.get(i - 1);
            console.print("|%3s", i);
            console.print("| %s ", selection.isAdded() ? "*" : " ");
            console.print("| %54s ", trimToSize(selection.getPlayground().getName(), 54));
            console.print("| %-11s |%n", selection.getPlayground().getMaxPlayers());
        }
        console.print("%s%n", SINGLE_LINE);

        console.print("1. add playground%n");
        console.print("2. remove playground%n");
        console.print("3. done%n");
    }

    @Override
    protected Menu execute(Choice choice) {
        int index = -1;
        if (choice != Choice.DONE) {
            index = parseChoice("Playground index (see table above): ") - 1;
            if (index < 0 || index >= playgrounds.size()) {
                return this;
            }
        }

        switch (choice) {
        case PLAYGROUND_ADD:
            playgrounds.get(index).setAdded(true);
            break;
        case PLAYGROUND_REMOVE:
            playgrounds.get(index).setAdded(false);
            break;
        case DONE:
            for (PlaygroundSelection playground : playgrounds) {
                if (playground.isAdded()) {
                    tournament.getPlaygrounds().add(playground.getPlayground());
                }
            }
            return new TournamentConfirmationMenu(console, client, tournament);
        default:
            return super.execute(choice);
        }

        return this;
    }

    private static class PlaygroundSelection {
        private final Playground playground;
        private boolean added;

        public PlaygroundSelection(Playground playground) {
            this(playground, false);
        }

        public PlaygroundSelection(Playground playground, boolean added) {
            this.playground = playground;
            this.added = added;
        }

        public Playground getPlayground() {
            return playground;
        }

        public boolean isAdded() {
            return added;
        }

        public void setAdded(boolean added) {
            this.added = added;
        }
    }
}
