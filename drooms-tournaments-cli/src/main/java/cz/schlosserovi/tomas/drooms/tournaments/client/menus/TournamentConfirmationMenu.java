package cz.schlosserovi.tomas.drooms.tournaments.client.menus;

import java.io.Console;

import cz.schlosserovi.tomas.drooms.tournaments.client.TournamentsServerClient;
import cz.schlosserovi.tomas.drooms.tournaments.domain.Tournament;

public class TournamentConfirmationMenu extends Menu {
    private final Tournament tournament;

    public TournamentConfirmationMenu(Console console, TournamentsServerClient client, Tournament tournament) {
        super(console, client);
        this.tournament = tournament;
    }

    @Override
    protected String getHeadline() {
        return "new tournament";
    }

    @Override
    protected boolean allowMainMenu() {
        return true;
    }

    @Override
    protected void printMenu() {
        TournamentDetailMenu.printTournamentDetail(console, tournament);
        console.format("1. create tournament%n");
        console.format("2. discard tournament%n");

    }

    @Override
    protected Menu execute(int choice) {
        if (choice == 1) {
            client.newTournament(tournament);
        }

        return new TournamentsMenu(console, client);
    }

}
