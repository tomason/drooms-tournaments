package org.drooms.tournaments.client.widget.playground.list;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.drooms.tournaments.client.page.PlaygroundDetailPage;
import org.drooms.tournaments.client.util.ApplicationContext;
import org.drooms.tournaments.domain.Playground;
import org.drooms.tournaments.services.PlaygroundService;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ui.nav.client.local.TransitionTo;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.common.collect.Multimaps;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

@Templated
public class PlaygroundsList extends Composite {
    @DataField
    private CellTable<Playground> playgrounds = new CellTable<Playground>(20);

    @Inject
    @DataField
    private Button refresh;

    @Inject
    @DataField
    private Button newPlayground;

    @Inject
    private Caller<PlaygroundService> service;

    @Inject
    private TransitionTo<PlaygroundDetailPage> detail;

    @Inject
    private ApplicationContext context;

    @PostConstruct
    public void init() {
        newPlayground.setVisible(context.isLoggedIn());
        newPlayground.setEnabled(context.isLoggedIn());

        Column<Playground, String> nameColumn = new Column<Playground, String>(new ClickableTextCell()) {
            @Override
            public String getValue(Playground object) {
                return object.getName();
            }
        };
        nameColumn.setFieldUpdater(new FieldUpdater<Playground, String>() {
            @Override
            public void update(int index, Playground object, String value) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("playgroundName", object.getName());
                detail.go(Multimaps.forMap(params));
            }
        });
        playgrounds.addColumn(nameColumn, "Name");

        playgrounds.addColumn(new Column<Playground, Number>(new NumberCell()) {
            @Override
            public Number getValue(Playground object) {
                return object.getMaxPlayers();
            }
        }, "Players");

        final NoSelectionModel<Playground> selection = new NoSelectionModel<Playground>();
        selection.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("playgroundName", selection.getLastSelectedObject().getName());
                detail.go(Multimaps.forMap(params));
            }
        });
        playgrounds.setSelectionModel(selection);

        playgrounds.flush();
        playgrounds.setWidth("100%");
        playgrounds.setColumnWidth(1, "100px");

        refresh();
    }

    @EventHandler("refresh")
    public void refreshClicked(ClickEvent event) {
        refresh();
    }

    @EventHandler("newPlayground")
    public void newPlaygroundClicked(ClickEvent event) {
        detail.go();
    }

    private void refresh() {
        service.call(new RemoteCallback<List<Playground>>() {
            @Override
            public void callback(List<Playground> response) {
                playgrounds.setRowData(0, response);
            }
        }).getPlaygrounds();
    }
}
