package org.drooms.tournaments.server.events;

import org.drooms.tournaments.server.data.model.TournamentEntity;

abstract class TournamentEvent {
    private final TournamentEntity tournament;

    public TournamentEvent(TournamentEntity tournament) {
        this.tournament = tournament;
    }

    public TournamentEntity getTournament() {
        return tournament;
    }

}
