package org.drooms.tournaments.client.widget.tournament.form;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.drooms.tournaments.client.event.ReloadPlaygroundsEvent;
import org.drooms.tournaments.client.page.PlaygroundDetailPage;
import org.drooms.tournaments.client.page.TournamentsPage;
import org.drooms.tournaments.client.page.UserDetailPage;
import org.drooms.tournaments.client.util.ApplicationContext;
import org.drooms.tournaments.client.util.Form;
import org.drooms.tournaments.client.util.FormMode;
import org.drooms.tournaments.client.widget.error.ErrorForm;
import org.drooms.tournaments.client.widget.spinner.Spinner;
import org.drooms.tournaments.client.widget.tournament.form.playgroundselector.PlaygroundSelector;
import org.drooms.tournaments.domain.Playground;
import org.drooms.tournaments.domain.Tournament;
import org.drooms.tournaments.domain.TournamentResult;
import org.drooms.tournaments.services.TournamentService;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.common.client.api.VoidCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.RestErrorCallback;
import org.jboss.errai.ui.nav.client.local.Navigation;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.common.collect.Multimaps;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

@Templated
public class TournamentForm extends Composite implements Form<Tournament> {
    private Tournament value;
    @Inject
    @DataField
    private ErrorForm error;

    @Inject
    @DataField
    private TextBox name;

    @Inject
    @DataField
    private DateBox start;

    @Inject
    @DataField
    private DateBox end;

    @Inject
    @DataField
    private Spinner period;

    @DataField
    private CellTable<Playground> playgrounds = new CellTable<Playground>(20);

    @Inject
    @DataField
    private Button editPlaygrounds;

    @DataField
    private CellTable<TournamentResult> results = new CellTable<TournamentResult>(20);

    @Inject
    @DataField
    private Button join;

    @DataField
    private Element playersRow = DOM.createTR();

    @Inject
    @DataField
    private Button refresh;

    @Inject
    @DataField
    private Button create;

    @Inject
    private ApplicationContext context;

    @Inject
    private Caller<TournamentService> service;

    @Inject
    private Navigation navigation;

    @Inject
    private PlaygroundSelector selector;

    private ListDataProvider<Playground> playgroundsList = new ListDataProvider<Playground>();
    private ListDataProvider<TournamentResult> resultsList = new ListDataProvider<TournamentResult>();

    @PostConstruct
    public void init() {
        start.setFormat(new DefaultFormat(ApplicationContext.DATE_FORMAT));
        end.setFormat(new DefaultFormat(ApplicationContext.DATE_FORMAT));
        period.setup(1, 1, null, 1);

        {// create playgrounds table
            playgrounds.addColumn(new Column<Playground, String>(new TextCell()) {
                @Override
                public String getValue(Playground object) {
                    return object.getName();
                }
            });
            final NoSelectionModel<Playground> selection = new NoSelectionModel<Playground>();
            selection.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
                @Override
                public void onSelectionChange(SelectionChangeEvent event) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("playgroundName", selection.getLastSelectedObject().getName());
                    navigation.goTo(PlaygroundDetailPage.class, Multimaps.forMap(map));
                }
            });
            playgrounds.setSelectionModel(selection);
    
            playgroundsList.addDataDisplay(playgrounds);
        }

        { // create results table
            results.addColumn(new Column<TournamentResult, Number>(new NumberCell()) {
                @Override
                public Number getValue(TournamentResult object) {
                    return object.getPosition();
                }
            }, "Position");
            results.addColumn(new Column<TournamentResult, String>(new TextCell()) {
                @Override
                public String getValue(TournamentResult object) {
                    return object.getPlayer().getName();
                }
            }, "Player");
            final NoSelectionModel<TournamentResult> selection = new NoSelectionModel<TournamentResult>();
            selection.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
                @Override
                public void onSelectionChange(SelectionChangeEvent event) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("userName", selection.getLastSelectedObject().getPlayer().getName());
                    navigation.goTo(UserDetailPage.class, Multimaps.forMap(map));
                }
            });
            results.setSelectionModel(selection);

            resultsList.addDataDisplay(results);
        }

        // add value change handler (for date change)
        ValueChangeHandler<Date> handler = new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<Date> event) {
                validate();
            }
        };

        start.addValueChangeHandler(handler);
        end.addValueChangeHandler(handler);
    }

    @Override
    public void setMode(FormMode mode) {
        name.setEnabled(mode == FormMode.NEW);
        start.setEnabled(mode == FormMode.NEW);
        end.setEnabled(mode == FormMode.NEW);
        period.setEnabled(mode == FormMode.NEW);

        editPlaygrounds.setVisible(mode == FormMode.NEW);

        if (mode == FormMode.NEW) {
            playersRow.getStyle().setDisplay(Display.NONE);
        }
        setJoinVisibility();

        refresh.setVisible(mode == FormMode.DETAIL);
        create.setVisible(mode == FormMode.NEW && context.isLoggedIn());
    }

    @Override
    public void setValue(Tournament value) {
        this.value = value;
        name.setValue(value.getName());
        start.setValue(value.getStart());
        end.setValue(value.getEnd());
        period.setValue(value.getPeriod());
        playgroundsList.setList(value.getPlaygrounds());
        Collections.sort(value.getResults());
        resultsList.setList(value.getResults());

        setJoinVisibility();
    }

    @EventHandler("join")
    public void joinTournamentClicked(ClickEvent event) {
        service.call(new RemoteCallback<Object>() {
            @Override
            public void callback(Object response) {
                service.call(new RemoteCallback<Tournament>() {
                    @Override
                    public void callback(Tournament response) {
                        setValue(response);
                    }
                }).getTournament(value.getName());
            }
        }).joinTournament(value);
    }

    @EventHandler("refresh")
    public void refreshClicked(ClickEvent event) {
        service.call(new RemoteCallback<Tournament>() {
            @Override
            public void callback(Tournament response) {
                setValue(response);
            }
        }).getTournament(value.getName());
    }

    @EventHandler("create")
    public void createClicked(ClickEvent event) {
        if (validate()) {
            Tournament newTournament = new Tournament();
            newTournament.setName(name.getValue().trim());
            newTournament.setStart(start.getValue());
            newTournament.setEnd(end.getValue());
            newTournament.setPeriod(period.getValue());
            newTournament.setPlaygrounds(playgroundsList.getList());

            service.call(new VoidCallback() {
                @Override
                public void callback(Void response) {
                    navigation.goTo(TournamentsPage.class, null);
                }
            }, new RestErrorCallback() {
                @Override
                public boolean error(Request message, Throwable throwable) {
                    error.addError("Tournament with this name already exists, choose a different name");

                    return false;
                }
            }).newTournament(newTournament);
        }
    }

    @EventHandler("editPlaygrounds")
    public void editPlaygroundsClicked(ClickEvent event) {
        selector.show(playgroundsList.getList());
    }

    @EventHandler("name")
    public void nameKeyPressed(KeyUpEvent event) {
        validate();
    }

    @EventHandler("start")
    public void startChanged(ChangeEvent event) {
        validate();
    }

    @EventHandler("end")
    public void endChanged(ChangeEvent event) {
        validate();
    }

    public void updatePlaygroundList(@Observes ReloadPlaygroundsEvent event) {
        playgroundsList.setList(selector.getValue());
        validate();
    }

    private void setJoinVisibility() {
        if (value != null && context.isLoggedIn()) {
            boolean visible = true;
            for (TournamentResult result : value.getResults()) {
                if (result.getPlayer().getName().equals(context.getUsername())) {
                    visible = false;
                    break;
                }
            }
            join.setEnabled(visible);
            join.setVisible(visible);
        } else {
            join.setEnabled(false);
            join.setVisible(false);
        }
    }

    private boolean validate() {
        error.clear();

        if (name.getValue().length() == 0) {
            error.addError("Name must not be empty");
        }
        if (start.getValue() == null) {
            error.addError("Start date must be filled in");
        }
        if (start.getValue() != null && start.getValue().before(new Date())) {
            error.addError("Start date must be in the future");
        }
        if (end.getValue() == null) {
            error.addError("End date must be filled in");
        }
        if (end.getValue() != null && start.getValue() != null && end.getValue().before(start.getValue())) {
            error.addError("End date must be after the start date");
        }
        if (playgroundsList.getList().size() < 1) {
            error.addError("The tournament must have at least one playground");
        }

        create.setEnabled(error.isValid());

        return error.isValid();
    }
}
