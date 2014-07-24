package org.drooms.tournaments.client.widget.tournament.form.playgroundselector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.drooms.tournaments.client.event.ReloadPlaygroundsEvent;
import org.drooms.tournaments.domain.Playground;
import org.drooms.tournaments.services.PlaygroundService;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

@Templated
public class PlaygroundSelector extends Composite {

    @DataField
    private CellTable<Playground> playgrounds = new CellTable<Playground>(20);
    private ListDataProvider<Playground> playgroundList = new ListDataProvider<Playground>();
    private List<Playground> selectedPlaygrounds = new ArrayList<Playground>();

    @Inject
    @DataField
    private Button selectAll;

    @Inject
    @DataField
    private Button selectNone;

    @Inject
    @DataField
    private Button apply;

    @Inject
    private Caller<PlaygroundService> service;

    @Inject
    private Event<ReloadPlaygroundsEvent> closedEvent;

    private PopupPanel popup = new PopupPanel(false, true);

    @PostConstruct
    public void init() {
        popup.setWidget(this);
        popup.setGlassEnabled(true);

        playgroundList.addDataDisplay(playgrounds);

        Column<Playground, Boolean> checkedColumn = new Column<Playground, Boolean>(new CheckboxCell()) {
            @Override
            public Boolean getValue(Playground object) {
                return selectedPlaygrounds.contains(object);
            }
        };
        checkedColumn.setFieldUpdater(new FieldUpdater<Playground, Boolean>() {
            @Override
            public void update(int index, Playground object, Boolean value) {
                if (value) {
                    selectedPlaygrounds.add(object);
                } else {
                    selectedPlaygrounds.remove(object);
                }
            }
        });
        playgrounds.addColumn(checkedColumn);
        playgrounds.addColumn(new Column<Playground, String>(new TextCell()) {
            @Override
            public String getValue(Playground object) {
                return object.getName();
            }
        }, "Name");
        playgrounds.addColumn(new Column<Playground, Number>(new NumberCell()) {
            @Override
            public Number getValue(Playground object) {
                return object.getMaxPlayers();
            }
        }, "Capacity");

        final NoSelectionModel<Playground> selection = new NoSelectionModel<Playground>();
        selection.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                if (selectedPlaygrounds.contains(selection.getLastSelectedObject())) {
                    selectedPlaygrounds.remove(selection.getLastSelectedObject());
                } else {
                    selectedPlaygrounds.add(selection.getLastSelectedObject());
                }
                playgrounds.flush();
                playgrounds.redraw();
            }
        });
        playgrounds.setSelectionModel(selection);
    }

    @EventHandler("selectAll")
    public void selectAllClicked(ClickEvent event) {
        selectedPlaygrounds.clear();
        selectedPlaygrounds.addAll(playgroundList.getList());
        playgrounds.flush();
        playgrounds.redraw();
    }

    @EventHandler("selectNone")
    public void selectNoneClicked(ClickEvent event) {
        selectedPlaygrounds.clear();
        playgrounds.flush();
        playgrounds.redraw();
    }

    @EventHandler("apply")
    public void applyClicked(ClickEvent event) {
        closedEvent.fire(new ReloadPlaygroundsEvent());
        popup.hide();
    }

    public void show(final List<Playground> selected) {
        selectedPlaygrounds.clear();
        selectedPlaygrounds.addAll(selected);

        service.call(new RemoteCallback<List<Playground>>() {
            @Override
            public void callback(List<Playground> response) {
                Collections.sort(response);
                playgroundList.setList(response);

                popup.center();
                popup.show();
            }
        }).getPlaygrounds();
    }

    public List<Playground> getValue() {
        return new ArrayList<Playground>(selectedPlaygrounds);
    }
}
