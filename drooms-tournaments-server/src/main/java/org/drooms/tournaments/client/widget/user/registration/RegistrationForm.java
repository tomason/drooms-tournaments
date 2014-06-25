package org.drooms.tournaments.client.widget.user.registration;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.drooms.tournaments.client.event.AuthEvent;
import org.drooms.tournaments.client.event.AuthEvent.AuthEventType;
import org.drooms.tournaments.client.page.LandingPage;
import org.drooms.tournaments.client.util.ApplicationContext;
import org.drooms.tournaments.client.widget.error.ErrorForm;
import org.drooms.tournaments.domain.User;
import org.drooms.tournaments.services.UserService;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.VoidCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.RestErrorCallback;
import org.jboss.errai.ui.nav.client.local.TransitionTo;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

@Templated
public class RegistrationForm extends Composite {
    @Inject
    @DataField
    private ErrorForm error;

    @Inject
    @DataField
    private TextBox username;

    @Inject
    @DataField
    private PasswordTextBox password;

    @Inject
    @DataField
    private PasswordTextBox passwordRepeat;

    @Inject
    @DataField
    private Button clear;

    @Inject
    @DataField
    private Button register;

    @Inject
    private Caller<UserService> service;

    @Inject
    private ApplicationContext context;

    @Inject
    private TransitionTo<LandingPage> home;

    @PostConstruct
    public void init() {
        register.setEnabled(false);
    }

    @EventHandler("clear")
    public void clearClicked(ClickEvent event) {
        username.setValue(null);
        password.setValue(null);
        passwordRepeat.setValue(null);

        register.setEnabled(false);
    }

    @EventHandler("register")
    public void registerClicked(ClickEvent event) {
        if (validate()) {
            final User newUser = new User(username.getValue(), password.getValue());
            service.call(new VoidCallback() {
                @Override
                public void callback(Void response) {
                    context.login(newUser.getName(), newUser.getPassword());
                }
            }, new RestErrorCallback() {
                @Override
                public boolean error(Request message, Throwable throwable) {
                    Window.alert(throwable.getMessage());
                    error.addError("User with this name is already registered");
                    error.setVisible(true);

                    return false;
                }
            }).register(newUser);
        }
    }

    @EventHandler("username")
    public void usernameKeyUp(KeyUpEvent event) {
        validate();
    }

    @EventHandler("password")
    public void passwordKeyUp(KeyUpEvent event) {
        validate();
    }

    @EventHandler("passwordRepeat")
    public void passwordRepeatKeyUp(KeyUpEvent event) {
        validate();
    }

    public void loggedIn(@Observes AuthEvent event) {
        if (event.getType() == AuthEventType.LOGGED_IN) {
            home.go();
        }
    }

    private boolean validate() {
        error.clear();
        if (username.getValue().length() < 3) {
            error.addError("User name must be at least 3 characters long.");
        }
        if (password.getValue().length() == 0) {
            error.addError("Password must not be empty.");
        }
        if (!passwordRepeat.getText().equals(password.getText())) {
            error.addError("Passwords don't match.");
        }

        register.setEnabled(error.isValid());

        return error.isValid();
    }
}
