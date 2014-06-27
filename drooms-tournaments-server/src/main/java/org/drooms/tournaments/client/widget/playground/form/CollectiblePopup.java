package org.drooms.tournaments.client.widget.playground.form;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.drooms.tournaments.client.event.CollectiblePopupClosed;
import org.drooms.tournaments.client.util.Collectible;
import org.drooms.tournaments.client.widget.error.ErrorForm;
import org.drooms.tournaments.client.widget.spinner.Spinner;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;

@Templated
public class CollectiblePopup extends Composite {
    @Inject
    @DataField
    private ErrorForm error;

    @Inject
    @DataField
    private TextBox id;

    @Inject
    @DataField
    private Spinner price;

    @Inject
    @DataField
    private Spinner expiration;

    @Inject
    @DataField
    private Spinner probability;

    @Inject
    @DataField
    private Button cancel;

    @Inject
    @DataField
    private Button ok;

    @Inject
    private Event<CollectiblePopupClosed> closedEvent;

    private PopupPanel popup = new PopupPanel(false, true);

    @PostConstruct
    public void init() {
        id.setValue(null);
        price.setup(1, 1, null, 5);
        expiration.setup(1, 1, null, 5);
        probability.setup(1, 1, 99, 5);

        popup.setGlassEnabled(true);
        popup.setWidget(this);
    }

    public void show() {
        init();
        popup.center();
        popup.show();
    }

    @EventHandler("cancel")
    public void cancelClicked(ClickEvent event) {
        popup.hide();
    }

    @EventHandler("ok")
    public void okClicked(ClickEvent event) {
        if (validate()) {
            popup.hide();

            closedEvent.fire(new CollectiblePopupClosed(new Collectible(id.getValue(), price.getValue(), expiration.getValue(),
                    probability.getValue() / 100.0)));
        }
    }

    @EventHandler("id")
    public void idKeyUp(KeyUpEvent event) {
        validate();
    }

    private boolean validate() {
        error.clear();

        if (id.getValue().length() == 0) {
            error.addError("Id must be at least 3 characters long.");
        }
        if (!id.getValue().matches("[a-zA-Z]*")) {
            error.addError("Use only characters for Id (a-zA-Z).");
        }

        return error.isValid();
    }
}
