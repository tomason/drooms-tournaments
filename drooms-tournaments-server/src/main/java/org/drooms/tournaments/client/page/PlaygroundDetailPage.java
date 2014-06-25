package org.drooms.tournaments.client.page;

import javax.inject.Inject;

import org.drooms.tournaments.client.util.ApplicationContext;
import org.drooms.tournaments.client.util.FormMode;
import org.drooms.tournaments.client.widget.playground.form.PlaygroundForm;
import org.drooms.tournaments.domain.Playground;
import org.drooms.tournaments.services.PlaygroundService;
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
public class PlaygroundDetailPage extends MyDefaultPage {
    @PageState
    private String playgroundName;

    @Inject
    @DataField
    private PlaygroundForm content;

    @Inject
    private ApplicationContext context;

    @Inject
    private Caller<PlaygroundService> service;

    @PageShowing
    public void init() {
        if (playgroundName != null && playgroundName.length() > 0) {
            content.setMode(FormMode.DETAIL);
            service.call(new RemoteCallback<Playground>() {
                @Override
                public void callback(Playground response) {
                    content.setValue(response);
                }
            }).getPlayground(playgroundName);
        } else if (context.isLoggedIn()) {
            content.setMode(FormMode.NEW);
        } else {
            navigation.goToWithRole(DefaultPage.class);
        }
    }
}
