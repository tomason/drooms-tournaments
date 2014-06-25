package org.drooms.tournaments.client.page;

import javax.inject.Inject;

import org.drooms.tournaments.client.util.ApplicationContext;
import org.drooms.tournaments.client.widget.user.form.UserForm;
import org.drooms.tournaments.domain.User;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageShowing;
import org.jboss.errai.ui.nav.client.local.TransitionTo;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Page
@Templated("MyDefaultPage.html#root")
public class UserDetailPage extends MyDefaultPage {

    // @PageState
    // private String userName;

    @Inject
    @DataField
    private UserForm content;

    @Inject
    private ApplicationContext context;

    @Inject
    private TransitionTo<LoginPage> login;

    @PageShowing
    public void init() {
        // FIXME there is no way to show other user than the logged in (no services to show other users stats)
        // if (userName == null || userName.length() == 0) {
        // userName = context.getUsername();
        // }
        // if (userName == null) {
        // home.go();
        // }
        if (!context.isLoggedIn()) {
            login.go();
        } else {
            content.setValue(new User(context.getUsername()));
        }
    }
}
