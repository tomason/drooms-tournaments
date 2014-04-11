package org.drooms.tournaments.server.events;

import org.drooms.tournaments.server.data.model.TournamentEntity;

public class NewTournamentEvent extends TournamentEvent {

    public NewTournamentEvent(TournamentEntity tournament) {
        super(tournament);
    }
}
