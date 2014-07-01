package org.drooms.tournaments.client.page;

import javax.inject.Inject;

import org.drooms.tournaments.client.util.ApplicationContext;
import org.drooms.tournaments.client.util.FormMode;
import org.drooms.tournaments.client.widget.tournament.form.TournamentForm;
import org.drooms.tournaments.domain.Tournament;
import org.drooms.tournaments.services.TournamentService;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageShowing;
import org.jboss.errai.ui.nav.client.local.PageState;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Page
@Templated("MyDefaultPage.html#root")
public class TournamentDetailPage extends MyDefaultPage {
    @PageState
    private String tournamentName;

    @Inject
    @DataField
    private TournamentForm content;

    @Inject
    private Caller<TournamentService> tournamentService;

    @Inject
    private ApplicationContext context;

    @PageShowing
    public void preparePage() {
        if (tournamentName != null && tournamentName.length() > 0) {
            content.setMode(FormMode.DETAIL);
            tournamentService.call(new RemoteCallback<Tournament>() {
                @Override
                public void callback(Tournament response) {
                    content.setValue(response);
                }
            }).getTournament(tournamentName);
        } else if (context.isLoggedIn()) {
            content.setMode(FormMode.NEW);
        } else {
            navigation.goToWithRole(DefaultPage.class);
        }
    }
}
