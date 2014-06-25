package org.drooms.tournaments.client.page;

import javax.inject.Inject;

import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageShowing;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.user.client.ui.Label;

@Page(role = DefaultPage.class)
@Templated("MyDefaultPage.html#root")
public class LandingPage extends MyDefaultPage {
    @Inject
    @DataField
    private Label content;

    @PageShowing
    public void init() {
        content.setText("These pages are currently under construction."
                + "You will see a lot of notices like this one on various pages."
                + "Read what they say and remember, this is still work in progress.");
    }
}
