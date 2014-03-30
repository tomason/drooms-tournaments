package cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.tournament;

import java.util.Set;

import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.Choice;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.menu.api.Menu;
import cz.schlosserovi.tomas.drooms.tournaments.client.interactive.util.OutputDevice;
import cz.schlosserovi.tomas.drooms.tournaments.client.services.TournamentsServerClient;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Tournament;
import cz.schlosserovi.tomas.drooms.tournaments.services.TournamentService;

class TournamentConfirmationMenu extends Menu {
    private final Tournament tournament;

    protected TournamentConfirmationMenu(OutputDevice console, TournamentsServerClient client, Tournament tournament) {
        super(console, client);
        this.tournament = tournament;
    }

    @Override
    protected String getHeadline() {
        return "new tournament";
    }

    @Override
    protected Set<Choice> getChoices() {
        return concatChoices(super.getChoices(), Choice.TOURNAMENT_CREATE, Choice.TOURNAMENT_DISCARD);
    }

    @Override
    protected void printInstructions() {
        TournamentDetailMenu.printTournamentDetail(console, tournament);
        console.print("1. create tournament%n");
        console.print("2. discard tournament%n");

    }

    @Override
    protected Menu execute(Choice choice) {
        switch (choice) {
        case TOURNAMENT_CREATE:
            client.getService(TournamentService.class).newTournament(tournament);
        case TOURNAMENT_DISCARD:
            return new TournamentsMenu(console, client);
        default:
            return super.execute(choice);
        }
    }

}
