package org.drooms.tournaments.client.page;

import javax.inject.Inject;

import org.drooms.tournaments.client.util.FormMode;
import org.drooms.tournaments.client.widget.game.detail.GameDetail;
import org.drooms.tournaments.domain.Game;
import org.drooms.tournaments.services.GameService;
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
public class GameDetailPage extends MyDefaultPage {
    @PageState
    private String gameId;

    @Inject
    @DataField
    private GameDetail content;

    @Inject
    private Caller<GameService> gameService;

    @PageShowing
    public void preparePage() {
        if (gameId != null && gameId.length() > 0) {
            content.setMode(FormMode.DETAIL);
            gameService.call(new RemoteCallback<Game>() {
                @Override
                public void callback(Game response) {
                    content.setValue(response);
                }
            }).getGame(gameId);
        } else {
            navigation.goToWithRole(DefaultPage.class);
        }
    }
}
