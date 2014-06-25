package org.drooms.tournaments.client.page;

import javax.inject.Inject;

import org.drooms.tournaments.client.widget.user.login.LoginForm;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Page(role = org.jboss.errai.ui.nav.client.local.api.LoginPage.class)
@Templated("MyDefaultPage.html#root")
public class LoginPage extends MyDefaultPage {
    @Inject
    @DataField
    private LoginForm content;

}
