package org.drooms.tournaments.client.widget.user.form;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.drooms.tournaments.client.event.ReloadStrategiesEvent;
import org.drooms.tournaments.client.util.ApplicationContext;
import org.drooms.tournaments.client.util.Form;
import org.drooms.tournaments.client.util.FormMode;
import org.drooms.tournaments.client.widget.user.form.newstrategy.StrategyPopup;
import org.drooms.tournaments.domain.Strategy;
import org.drooms.tournaments.domain.User;
import org.drooms.tournaments.services.StrategyService;
import org.drooms.tournaments.services.UserService;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.common.client.api.VoidCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.RestErrorCallback;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

@Templated
public class UserForm extends Composite implements Form<User> {
    private User user;

    @Inject
    @DataField
    private TextBox username;

    @Inject
    @DataField
    private Button changePassword;

    @Inject
    @DataField
    private Label error;

    @Inject
    @DataField
    private PasswordTextBox newPassword;

    @Inject
    @DataField
    private PasswordTextBox newPasswordRepeat;

    @Inject
    @DataField
    private Button savePassword;

    @DataField
    private CellTable<Strategy> strategies = new CellTable<Strategy>(20);

    @Inject
    @DataField
    private Button newStrategy;

    @DataField
    private Element passwordRow = DOM.createTR();

    @Inject
    private ApplicationContext context;

    @Inject
    private Caller<UserService> userService;

    @Inject
    private Caller<StrategyService> strategyService;

    @Inject
    private StrategyPopup strategyPopup;

    @PostConstruct
    public void init() {
        strategies.addColumn(new Column<Strategy, String>(new TextCell()) {
            @Override
            public String getValue(Strategy object) {
                return object.getGav().getGroupId();
            }
        }, "Group Id");
        strategies.addColumn(new Column<Strategy, String>(new TextCell()) {
            @Override
            public String getValue(Strategy object) {
                return object.getGav().getArtifactId();
            }
        }, "Artifact Id");
        strategies.addColumn(new Column<Strategy, String>(new TextCell()) {
            @Override
            public String getValue(Strategy object) {
                return object.getGav().getVersion();
            }
        }, "Version");
        Column<Strategy, Boolean> activeColumn = new Column<Strategy, Boolean>(new CheckboxCell()) {
            @Override
            public Boolean getValue(Strategy object) {
                return object.isActive();
            }
        };
        activeColumn.setFieldUpdater(new FieldUpdater<Strategy, Boolean>() {
            @Override
            public void update(int index, Strategy object, Boolean value) {
                strategyService.call(new VoidCallback() {
                    @Override
                    public void callback(Void response) {
                        refreshStrategies();
                    }
                }).setActiveStrategy(object);
            }
        });
        strategies.addColumn(activeColumn, "Active");
    }

    @EventHandler("changePassword")
    public void changePasswordClicked(ClickEvent event) {
        changePassword.setVisible(false);

        newPassword.setVisible(true);
        newPasswordRepeat.setVisible(true);
        savePassword.setVisible(true);
    }

    @EventHandler("newPassword")
    public void newPasswordKeyUp(KeyUpEvent event) {
        validatePassword();
    }

    @EventHandler("newPasswordRepeat")
    public void newPasswordRepeatKeyUp(KeyUpEvent event) {
        validatePassword();
    }

    @EventHandler("savePassword")
    public void savePasswordClicked(ClickEvent event) {
        if (validatePassword()) {
            final User user = new User(this.user.getName(), newPassword.getValue());

            userService.call(new VoidCallback() {
                @Override
                public void callback(Void response) {
                    changePassword.setVisible(true);

                    error.setVisible(false);
                    newPassword.setVisible(false);
                    newPasswordRepeat.setVisible(false);
                    savePassword.setVisible(false);

                    context.login(user.getName(), user.getPassword());
                }
            }, new RestErrorCallback() {
                @Override
                public boolean error(Request message, Throwable throwable) {
                    error.setText("Unable to change password");
                    error.setVisible(true);

                    return false;
                }
            }).changePassword(user);
        }
    }

    @EventHandler("newStrategy")
    public void newStrategyClicked(ClickEvent event) {
        strategyPopup.show();
    }

    public void setValue(User user) {
        this.user = user;
        username.setText(user.getName());
        username.setEnabled(false);

        boolean loggedIn = user.getName().equals(context.getUsername());
        if (loggedIn) {
            passwordRow.getStyle().clearDisplay();
        } else {
            passwordRow.getStyle().setDisplay(Display.NONE);

            strategies.removeColumn(3);
        }
        newStrategy.setVisible(loggedIn);

        refreshStrategies();
    }

    @Override
    public void setMode(FormMode mode) {
        // currently only detail mode is used
        if (mode == FormMode.DETAIL) {
            error.setVisible(false);
            newPassword.setVisible(false);
            newPasswordRepeat.setVisible(false);
            savePassword.setVisible(false);
            savePassword.setEnabled(false);
        }
    }

    public void strategiesChanged(@Observes ReloadStrategiesEvent event) {
        refreshStrategies();
    }

    private boolean validatePassword() {
        boolean valid = true;
        String errorMsg = "";
        if (newPassword.getValue().length() == 0) {
            valid = false;
            errorMsg = (errorMsg.length() == 0 ? "" : "\n") + "Password must not be empty.";
        }
        if (!newPassword.getValue().equals(newPasswordRepeat.getValue())) {
            valid = false;
            errorMsg = (errorMsg.length() == 0 ? "" : "\n") + "Password don't match.";
        }

        error.setVisible(!valid);
        error.setText(errorMsg);
        savePassword.setEnabled(valid);

        return newPassword.getValue().equals(newPasswordRepeat.getValue()) && newPassword.getValue().length() > 0;
    }

    private void refreshStrategies() {
        strategyService.call(new RemoteCallback<List<Strategy>>() {
            @Override
            public void callback(List<Strategy> response) {
                Collections.sort(response);
                strategies.setRowData(response);
            }
        }).getStrategies(user.getName());
    }
}
