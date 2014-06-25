package org.drooms.tournaments.client.widget.playground.form;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.drooms.tournaments.client.page.PlaygroundsPage;
import org.drooms.tournaments.client.util.Form;
import org.drooms.tournaments.client.util.FormMode;
import org.drooms.tournaments.client.widget.error.ErrorForm;
import org.drooms.tournaments.domain.Playground;
import org.drooms.tournaments.services.PlaygroundService;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

@Templated
public class PlaygroundForm extends Composite implements Form<Playground> {
    @Inject
    @DataField
    private ErrorForm error;

    @Inject
    @DataField
    private TextBox name;

    @Inject
    @DataField
    private TextArea properties;

    @Inject
    @DataField
    private TextArea source;

    @Inject
    @DataField
    private Button create;

    @Inject
    private Caller<PlaygroundService> service;

    @Inject
    private TransitionTo<PlaygroundsPage> playgrounds;

    @Override
    public void setMode(FormMode mode) {
        name.setEnabled(mode == FormMode.NEW);
        properties.setEnabled(mode == FormMode.NEW);
        source.setEnabled(mode == FormMode.NEW);
        create.setEnabled(mode == FormMode.NEW);
        create.setVisible(mode == FormMode.NEW);
    }

    @Override
    public void setValue(Playground value) {
        name.setValue(value.getName());
        source.setValue(value.getSource());

        if (value.getConfiguration() != null) {
            StringBuilder configuration = new StringBuilder();
            for (Entry<String, String> entry : value.getConfiguration().entrySet()) {
                if (configuration.length() > 0) {
                    configuration.append("\n");
                }
                configuration.append(entry.getKey()).append("=").append(entry.getValue());
            }
            properties.setValue(configuration.toString());
        }

        error.clear();
    }

    @EventHandler("create")
    public void createClicked(ClickEvent event) {
        if (validate()) {
            Playground p = new Playground();
            p.setName(name.getValue());
            p.setSource(source.getValue());

            Map<String, String> configuration = new HashMap<String, String>();
            for (String line : properties.getValue().split("\n")) {
                String[] parsed = line.split("=");
                if (parsed.length == 2) {
                    configuration.put(parsed[0], parsed[1]);
                }
            }
            p.setConfiguration(configuration);

            service.call(new VoidCallback() {
                @Override
                public void callback(Void response) {
                    playgrounds.go();
                }
            }, new RestErrorCallback() {
                @Override
                public boolean error(Request message, Throwable throwable) {
                    error.addError("Playground with this name already exists, choose a different name.");

                    return false;
                }
            }).newPlayground(p);
        }
    }

    @EventHandler("name")
    public void nameKeyUp(KeyUpEvent event) {
        validate();
    }

    @EventHandler("source")
    public void sourceKeyUp(KeyUpEvent event) {
        validate();
    }

    private boolean validate() {
        error.clear();

        if (name.getValue().length() == 0) {
            error.addError("Playground name must not be empty.");
        }
        String s = source.getValue();
        if (s.length() == 0) {
            error.addError("Playground source must not be empty.");
        }
        if (s.length() - s.replaceAll("@", "").length() < 2) {
            error.addError("Playground must have at least two starting positions.");
        }

        create.setEnabled(error.isValid());

        return error.isValid();
    }
}
