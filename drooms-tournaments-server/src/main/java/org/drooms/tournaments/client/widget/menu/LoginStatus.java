package org.drooms.tournaments.client.widget.menu;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.drooms.tournaments.client.event.AuthEvent;
import org.drooms.tournaments.client.page.LandingPage;
import org.drooms.tournaments.client.page.LoginPage;
import org.drooms.tournaments.client.util.ApplicationContext;
import org.jboss.errai.ui.nav.client.local.TransitionAnchor;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;

@Templated
public class LoginStatus extends Composite {

    @DataField
    private Element text = DOM.createSpan();
    @DataField
    private Element not = DOM.createSpan();
    @DataField
    private Element as = DOM.createSpan();

    @Inject
    @DataField
    private TransitionAnchor<LoginPage> login;

    @Inject
    @DataField
    private TransitionAnchor<LandingPage> logout;

    @Inject
    private ApplicationContext context;

    @PostConstruct
    public void init() {
        refresh();
    }

    @EventHandler("logout")
    public void onLogout(ClickEvent event) {
        context.logout();
        logout.click();
    }

    public void statusChange(@Observes AuthEvent event) {
        refresh();
    }

    public void refresh() {
        if (context.isLoggedIn()) {
            text.setInnerText(context.getUsername());

            not.getStyle().setDisplay(Display.NONE);
            as.getStyle().setDisplay(Display.INLINE);
            text.getStyle().setDisplay(Display.INLINE);

            login.setVisible(false);
            logout.setVisible(true);
        } else {
            text.setInnerText("");

            not.getStyle().setDisplay(Display.INLINE);
            as.getStyle().setDisplay(Display.NONE);
            text.getStyle().setDisplay(Display.NONE);

            login.setVisible(true);
            logout.setVisible(false);
        }
    }
}
