package org.drooms.tournaments.client.page;

import org.drooms.tournaments.client.widget.game.list.GamesList;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageShown;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.inject.Inject;

@Page
@Templated("MyDefaultPage.html#root")
public class GamesPage extends MyDefaultPage {
    @Inject
    @DataField
    private GamesList content;

    @PageShown
    public void init() {
        content.reload();
    }
}
