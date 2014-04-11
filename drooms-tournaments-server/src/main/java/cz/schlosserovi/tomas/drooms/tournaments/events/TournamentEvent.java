package cz.schlosserovi.tomas.drooms.tournaments.events;

import cz.schlosserovi.tomas.drooms.tournaments.data.model.TournamentEntity;

abstract class TournamentEvent {
    private final TournamentEntity tournament;

    public TournamentEvent(TournamentEntity tournament) {
        this.tournament = tournament;
    }

    public TournamentEntity getTournament() {
        return tournament;
    }

}
