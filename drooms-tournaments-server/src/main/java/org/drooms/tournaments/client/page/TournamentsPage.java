package org.drooms.tournaments.client.page;

import javax.inject.Inject;

import org.drooms.tournaments.client.widget.tournament.list.TournamentsList;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Page
@Templated("MyDefaultPage.html#root")
public class TournamentsPage extends MyDefaultPage {
    @Inject
    @DataField
    private TournamentsList content;
}
