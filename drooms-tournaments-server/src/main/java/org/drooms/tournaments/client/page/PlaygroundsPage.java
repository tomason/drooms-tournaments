package org.drooms.tournaments.client.page;

import javax.inject.Inject;

import org.drooms.tournaments.client.widget.playground.list.PlaygroundsList;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Page
@Templated("MyDefaultPage.html#root")
public class PlaygroundsPage extends MyDefaultPage {
    @Inject
    @DataField
    private PlaygroundsList content;
}