package org.drooms.tournaments.client.interactive.menu.tournament;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.drooms.tournaments.client.interactive.menu.Choice;
import org.drooms.tournaments.client.interactive.menu.api.Menu;
import org.drooms.tournaments.client.interactive.util.OutputDevice;
import org.drooms.tournaments.client.services.TournamentsServerClient;
import org.drooms.tournaments.domain.Playground;
import org.drooms.tournaments.domain.Tournament;
import org.drooms.tournaments.services.PlaygroundService;

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
        console.printLine(singleLine());
        console.printLine("|%3s|%3s| %54s | %11s |", "", "", "name", "max players");
        console.printLine(singleLine());
        for (int i = 1; i <= playgrounds.size(); i++) {
            PlaygroundSelection selection = playgrounds.get(i - 1);
            console.print("|%3s", i);
            console.print("| %s ", selection.isAdded() ? "*" : " ");
            console.print("| %54s ", trimToSize(selection.getPlayground().getName(), 54));
            console.printLine("| %-11s |", selection.getPlayground().getMaxPlayers());
        }
        console.printLine(singleLine());
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
