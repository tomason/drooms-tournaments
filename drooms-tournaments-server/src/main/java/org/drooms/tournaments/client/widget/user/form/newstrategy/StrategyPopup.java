package org.drooms.tournaments.client.widget.user.form.newstrategy;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.drooms.tournaments.client.event.ReloadStrategiesEvent;
import org.drooms.tournaments.domain.Strategy;
import org.drooms.tournaments.services.StrategyService;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.VoidCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.RestErrorCallback;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;

@Templated
public class StrategyPopup extends Composite {
    @Inject
    @DataField
    private Label error;

    @Inject
    @DataField
    private TextBox groupId;

    @Inject
    @DataField
    private TextBox artifactId;

    @Inject
    @DataField
    private TextBox version;

    @Inject
    @DataField
    private CheckBox activate;

    @Inject
    @DataField
    private Button save;

    @Inject
    @DataField
    private Button cancel;

    @Inject
    private Caller<StrategyService> service;

    @Inject
    private Event<ReloadStrategiesEvent> reloadStrategies;

    private PopupPanel popup = new PopupPanel(false, true);

    @PostConstruct
    public void init() {
        popup.setWidget(this);
        popup.setGlassEnabled(true);
    }

    @EventHandler("save")
    public void saveClicked(ClickEvent event) {
        Strategy strategy = new Strategy(groupId.getValue(), artifactId.getValue(), version.getValue());
        strategy.setActive(activate.getValue());

        if (validate()) {
            service.call(new VoidCallback() {
                @Override
                public void callback(Void response) {
                    reloadStrategies.fire(new ReloadStrategiesEvent());
                    popup.hide();
                }
            }, new RestErrorCallback() {
                @Override
                public boolean error(Request message, Throwable throwable) {
                    error.setText("Unable to create new strategy");
                    error.setVisible(true);

                    return false;
                }
            }).newStrategy(strategy);
        }
    }

    @EventHandler("cancel")
    public void cancelClicked(ClickEvent event) {
        popup.hide();
    }

    @EventHandler("groupId")
    public void groupIdKeyUp(KeyUpEvent event) {
        validate();
    }

    @EventHandler("artifactId")
    public void artifactIdKeyUp(KeyUpEvent event) {
        validate();
    }

    @EventHandler("version")
    public void versionKeyUp(KeyUpEvent event) {
        validate();
    }

    public void show() {
        groupId.setValue(null);
        artifactId.setValue(null);
        version.setValue(null);
        activate.setValue(false);

        save.setEnabled(false);
        error.setVisible(false);

        popup.center();
        popup.show();
    }

    private boolean validate() {
        String errorMsg = "";

        if (groupId.getValue().length() == 0) {
            errorMsg = (errorMsg.length() == 0 ? "" : "\n") + "Group Id must not be empty";
        }
        if (artifactId.getValue().length() == 0) {
            errorMsg = (errorMsg.length() == 0 ? "" : "\n") + "Artifact Id must not be empty";
        }
        if (version.getValue().length() == 0) {
            errorMsg = (errorMsg.length() == 0 ? "" : "\n") + "Version must not be empty";
        }

        save.setEnabled(errorMsg.length() == 0);
        error.setVisible(errorMsg.length() > 0);
        error.setText(errorMsg);

        return errorMsg.length() == 0;
    }
}
