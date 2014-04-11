package org.drooms.tournaments.server.events;

import org.drooms.tournaments.server.data.model.TournamentEntity;

public class UpdatedTournamentEvent extends TournamentEvent {

    public UpdatedTournamentEvent(TournamentEntity tournament) {
        super(tournament);
    }

}
