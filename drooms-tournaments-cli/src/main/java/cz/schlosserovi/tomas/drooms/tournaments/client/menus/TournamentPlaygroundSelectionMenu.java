package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

import java.io.Console;
import java.util.LinkedList;
import java.util.List;

import cz.schlosserovi.tomas.drooms.tournaments.client.TournamentsServerClient;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Playground;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Tournament;

class TournamentPlaygroundSelectionMenu extends Menu {
    private final Tournament tournament;
    private final List<PlaygroundSelection> playgrounds = new LinkedList<>();

    public TournamentPlaygroundSelectionMenu(Console console, TournamentsServerClient client, Tournament tournament) {
        super(console, client);
        this.tournament = tournament;
        tournament.setPlaygrounds(new LinkedList<Playground>());
        for (Playground playground : client.getAllPlaygrounds()) {
            playgrounds.add(new PlaygroundSelection(playground));
        }
    }

    @Override
    protected boolean allowMainMenu() {
        return false;
    }

    @Override
    protected String getHeadline() {
        return "new tournament";
    }

    @Override
    protected void printMenu() {
        console.format("%s%n", SINGLE_LINE);
        console.format("|%3s|%3s| %54s | %11s |%n", "", "", "name", "max players");
        console.format("%s%n", SINGLE_LINE);
        for (int i = 1; i <= playgrounds.size(); i++) {
            PlaygroundSelection selection = playgrounds.get(i - 1);
            console.format("|%3s", i);
            console.format("| %s ", selection.isAdded() ? "*" : " ");
            console.format("| %54s ", trimToSize(selection.getPlayground().getName(), 54));
            console.format("| %-11s |%n", selection.getPlayground().getMaxPlayers());
        }
        console.format("%s%n", SINGLE_LINE);

        console.format("1. add playground%n");
        console.format("2. remove playground%n");
        console.format("3. done%n");
    }

    @Override
    protected Menu execute(int choice) {
        int index = -1;
        if (choice < 3) {
            index = parseChoice("Playground index (see table above): ") - 1;
            if (index < 0 || index >= playgrounds.size()) {
                return this;
            }
        }

        switch (choice) {
        case 1:
            playgrounds.get(index).setAdded(true);
            break;
        case 2:
            playgrounds.get(index).setAdded(false);
            break;
        case 3:
            for (PlaygroundSelection playground : playgrounds) {
                if (playground.isAdded()) {
                    tournament.getPlaygrounds().add(playground.getPlayground());
                }
            }
            return new TournamentConfirmationMenu(console, client, tournament);
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
