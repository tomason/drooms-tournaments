package org.drooms.tournaments.client.widget.playground.form;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.drooms.tournaments.client.event.CollectiblePopupClosed;
import org.drooms.tournaments.client.page.PlaygroundsPage;
import org.drooms.tournaments.client.util.Collectible;
import org.drooms.tournaments.client.util.Form;
import org.drooms.tournaments.client.util.FormMode;
import org.drooms.tournaments.client.widget.error.ErrorForm;
import org.drooms.tournaments.client.widget.playground.editor.PlaygroundEditor;
import org.drooms.tournaments.client.widget.spinner.Spinner;
import org.drooms.tournaments.domain.Playground;
import org.drooms.tournaments.services.PlaygroundService;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.VoidCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.RestErrorCallback;
import org.jboss.errai.ui.nav.client.local.TransitionTo;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.ListDataProvider;

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
    private Spinner startLength;

    @Inject
    @DataField
    private Spinner turns;

    @Inject
    @DataField
    private Spinner bonus;

    @Inject
    @DataField
    private Spinner timeout;

    @Inject
    @DataField
    private Spinner inactive;

    @DataField
    private CellTable<Collectible> collectibles = new CellTable<Collectible>(20);
    private ListDataProvider<Collectible> collectiblesList = new ListDataProvider<Collectible>();

    @Inject
    @DataField
    private Button addCollectible;

    @Inject
    @DataField
    private PlaygroundEditor editor;

    @Inject
    @DataField
    private Button create;

    @Inject
    private Caller<PlaygroundService> service;

    @Inject
    private TransitionTo<PlaygroundsPage> playgrounds;

    @Inject
    private CollectiblePopup collectiblePopup;

    @PostConstruct
    public void init() {
        collectiblesList.addDataDisplay(collectibles);

        collectibles.addColumn(new Column<Collectible, String>(new TextCell()) {
            @Override
            public String getValue(Collectible object) {
                return object.getId();
            }
        }, "Id");
        collectibles.addColumn(new Column<Collectible, Number>(new NumberCell()) {
            @Override
            public Number getValue(Collectible object) {
                return object.getPrice();
            }
        }, "Price");
        collectibles.addColumn(new Column<Collectible, Number>(new NumberCell()) {
            @Override
            public Number getValue(Collectible object) {
                return object.getExpiration();
            }
        }, "Expiration");
        collectibles.addColumn(new Column<Collectible, String>(new TextCell()) {
            @Override
            public String getValue(Collectible object) {
                return Double.toString(object.getProbability() * 100) + "%";
            }
        }, "Probability");
    }

    @Override
    public void setMode(FormMode mode) {
        name.setEnabled(mode == FormMode.NEW);
        create.setEnabled(mode == FormMode.NEW);
        create.setVisible(mode == FormMode.NEW);
        addCollectible.setVisible(mode == FormMode.NEW);

        startLength.setEnabled(mode == FormMode.NEW);
        startLength.setup(null, 1, null, 1);
        turns.setEnabled(mode == FormMode.NEW);
        turns.setup(null, 1, null, 100);
        bonus.setEnabled(mode == FormMode.NEW);
        bonus.setup(null, 0, null, 1);
        timeout.setEnabled(mode == FormMode.NEW);
        timeout.setup(null, 1, null, 1);
        inactive.setEnabled(mode == FormMode.NEW);
        inactive.setup(null, 1, null, 1);

        if (mode == FormMode.NEW) {
            Column<Collectible, String> removeColumn = new Column<Collectible, String>(new ButtonCell()) {
                @Override
                public String getValue(Collectible object) {
                    return "Remove";
                }
            };
            removeColumn.setFieldUpdater(new FieldUpdater<Collectible, String>() {
                @Override
                public void update(int index, Collectible object, String value) {
                    collectiblesList.getList().remove(object);
                }
            });
            collectibles.addColumn(removeColumn);
        }

        editor.setMode(mode);

        setConfiguration(null);
    }

    @Override
    public void setValue(Playground value) {
        name.setValue(value.getName());
        editor.setValue(value.getSource());

        setConfiguration(value.getConfiguration());

        error.clear();
    }

    @EventHandler("create")
    public void createClicked(ClickEvent event) {
        if (validate()) {
            Playground p = new Playground();
            p.setName(name.getValue());
            p.setSource(editor.getValue());
            p.setConfiguration(getConfiguration());

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

    @EventHandler("addCollectible")
    public void addCollectibleClicked(ClickEvent event) {
        collectiblePopup.show();
    }

    public void addCollectible(@Observes CollectiblePopupClosed event) {
        collectiblesList.getList().add(event.getCollectible());
        validate();
    }

    private boolean validate() {
        error.clear();

        if (name.getValue().length() == 0) {
            error.addError("Playground name must not be empty.");
        }
        String s = editor.getValue();
        if (s.length() == 0) {
            error.addError("Playground source must not be empty.");
        }
        if (s.length() - s.replaceAll("@", "").length() < 2) {
            error.addError("Playground must have at least two starting positions.");
        }
        if (collectiblesList.getList().size() == 0) {
            error.addError("At least one collectible must be defined.");
        }

        create.setEnabled(error.isValid());

        return error.isValid();
    }

    private void setConfiguration(Map<String, String> configuration) {
        if (configuration == null) {
            configuration = Collections.emptyMap();
        }

        String property;
        // parse worm.* properties
        property = configuration.get("worm.length.start");
        startLength.setValue(property != null ? Integer.parseInt(property) : 3);
        property = configuration.get("worm.max.turns");
        turns.setValue(property != null ? Integer.parseInt(property) : 1000);
        property = configuration.get("worm.survival.bonus");
        bonus.setValue(property != null ? Integer.parseInt(property) : 5);
        property = configuration.get("worm.timeout.seconds");
        timeout.setValue(property != null ? Integer.parseInt(property) : 1);
        property = configuration.get("worm.max.inactive.turns");
        inactive.setValue(property != null ? Integer.parseInt(property) : 3);

        property = configuration.get("collectibles");
        if (property != null && property.length() > 0) {
            for (String collectible : property.split(",")) {
                collectiblesList.getList().add(new Collectible(collectible, configuration));
            }
        }
    }

    private Map<String, String> getConfiguration() {
        Map<String, String> result = new HashMap<String, String>();

        result.put("worm.length.start", Integer.toString(startLength.getValue()));
        result.put("worm.max.turns", Integer.toString(turns.getValue()));
        result.put("worm.survival.bonus", Integer.toString(bonus.getValue()));
        result.put("worm.timeout.seconds", Integer.toString(timeout.getValue()));
        result.put("worm.max.inactive.turns", Integer.toString(inactive.getValue()));

        for (Collectible collectible : collectiblesList.getList()) {
            collectible.addToConfiguration(result);
        }

        return result;
    }
}
