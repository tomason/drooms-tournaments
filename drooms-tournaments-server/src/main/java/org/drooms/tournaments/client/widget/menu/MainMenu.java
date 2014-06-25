package org.drooms.tournaments.client.widget.menu;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.drooms.tournaments.client.page.LandingPage;
import org.drooms.tournaments.client.page.LoginPage;
import org.drooms.tournaments.client.page.PlaygroundsPage;
import org.drooms.tournaments.client.page.RegistrationPage;
import org.drooms.tournaments.client.page.TournamentsPage;
import org.drooms.tournaments.client.page.UserDetailPage;
import org.drooms.tournaments.client.util.ApplicationContext;
import org.jboss.errai.ui.nav.client.local.TransitionAnchor;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.user.client.ui.Composite;

@Templated
public class MainMenu extends Composite {
    @Inject
    private ApplicationContext context;

    @Inject
    @DataField
    private TransitionAnchor<LandingPage> home;

    @Inject
    @DataField
    private TransitionAnchor<UserDetailPage> user;

    @Inject
    @DataField
    private TransitionAnchor<TournamentsPage> tournaments;

    @Inject
    @DataField
    private TransitionAnchor<PlaygroundsPage> playgrounds;

    @Inject
    @DataField
    private TransitionAnchor<RegistrationPage> registration;

    @Inject
    @DataField
    private TransitionAnchor<LoginPage> login;

    @PostConstruct
    public void init() {
        user.setVisible(context.isLoggedIn());
        registration.setVisible(!context.isLoggedIn());
        login.setVisible(!context.isLoggedIn());
    }
}
