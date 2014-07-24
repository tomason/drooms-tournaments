package org.drooms.tournaments.client.widget.tournament.list;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.drooms.tournaments.client.page.TournamentDetailPage;
import org.drooms.tournaments.client.util.ApplicationContext;
import org.drooms.tournaments.domain.Tournament;
import org.drooms.tournaments.services.TournamentService;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ui.nav.client.local.TransitionTo;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.common.collect.Multimaps;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

@Templated
public class TournamentsList extends Composite {
    @DataField
    private CellTable<Tournament> tournaments = new CellTable<Tournament>(20);

    @Inject
    @DataField
    private Button refresh;

    @Inject
    @DataField
    private Button newTournament;

    @Inject
    private Caller<TournamentService> service;

    @Inject
    private ApplicationContext context;

    @Inject
    private TransitionTo<TournamentDetailPage> detail;

    @PostConstruct
    public void init() {
        newTournament.setEnabled(context.isLoggedIn());
        newTournament.setVisible(context.isLoggedIn());

        tournaments.addColumn(new Column<Tournament, String>(new ClickableTextCell()) {
            @Override
            public String getValue(Tournament object) {
                return object.getName();
            }
        }, "Name");
        tournaments.addColumn(new Column<Tournament, Date>(new DateCell(ApplicationContext.DATE_FORMAT)) {
            @Override
            public Date getValue(Tournament object) {
                return object.getStart();
            }
        }, "Start date");
        tournaments.addColumn(new Column<Tournament, Date>(new DateCell(ApplicationContext.DATE_FORMAT)) {
            @Override
            public Date getValue(Tournament object) {
                return object.getEnd();
            }
        }, "End date");
        tournaments.addColumn(new Column<Tournament, Number>(new NumberCell()) {
            @Override
            public Number getValue(Tournament object) {
                return object.getPeriod();
            }
        }, "Period");

        final NoSelectionModel<Tournament> selection = new NoSelectionModel<Tournament>();
        selection.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tournamentName", selection.getLastSelectedObject().getName());
                detail.go(Multimaps.forMap(params));
            }
        });
        tournaments.setSelectionModel(selection);

        tournaments.setWidth("100%");
        tournaments.setColumnWidth(1, "100px");
        tournaments.setColumnWidth(2, "100px");
        tournaments.setColumnWidth(3, "50px");
        tournaments.flush();

        refresh();
    }

    @EventHandler("refresh")
    public void refreshClicked(ClickEvent event) {
        refresh();
    }

    @EventHandler("newTournament")
    public void newTournamentClicked(ClickEvent event) {
        detail.go();
    }

    private void refresh() {
        service.call(new RemoteCallback<List<Tournament>>() {
            @Override
            public void callback(List<Tournament> response) {
                tournaments.setRowData(0, response);
            }
        }).getTournaments();
    }
}
