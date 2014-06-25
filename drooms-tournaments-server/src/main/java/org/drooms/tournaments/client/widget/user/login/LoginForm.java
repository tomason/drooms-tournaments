package org.drooms.tournaments.client.widget.user.login;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.drooms.tournaments.client.event.AuthEvent;
import org.drooms.tournaments.client.page.LandingPage;
import org.drooms.tournaments.client.util.ApplicationContext;
import org.jboss.errai.ui.client.widget.AbstractForm;
import org.jboss.errai.ui.nav.client.local.TransitionTo;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.FormElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

@Templated
public class LoginForm extends AbstractForm {
    @Inject
    @DataField
    private TextBox username;

    @Inject
    @DataField
    private PasswordTextBox password;

    @Inject
    @DataField
    private Button login;

    @Inject
    @DataField
    private Label alert;

    @DataField
    private FormElement form = Document.get().createFormElement();

    @Inject
    private TransitionTo<LandingPage> home;

    @Inject
    private ApplicationContext context;

    @PostConstruct
    public void init() {
        alert.setVisible(false);
    }

    @EventHandler("login")
    public void loginClicked(ClickEvent event) {
        context.login(username.getText(), password.getText());
    }

    public void onAuthEvent(@Observes AuthEvent event) {
        switch (event.getType()) {
        case LOGGED_IN:
            home.go();
            break;
        case FAILED_AUTH:
            alert.setVisible(true);
            break;
        default:
            // do nothing
            break;
        }
    }

    @Override
    protected FormElement getFormElement() {
        return form;
    }
}
