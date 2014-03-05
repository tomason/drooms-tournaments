package cz.schlosserovi.tomas.drooms.tournaments.events;

import cz.schlosserovi.tomas.drooms.tournaments.model.TournamentEntity;

public class NewTournamentEvent {
    private final TournamentEntity tournament;

    public NewTournamentEvent(TournamentEntity tournament) {
        this.tournament = tournament;
    }

    public TournamentEntity getTournament() {
        return tournament;
    }
}
