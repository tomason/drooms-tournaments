package org.drooms.tournaments.client.page;

import javax.inject.Inject;

import org.drooms.tournaments.client.widget.menu.LoginStatus;
import org.drooms.tournaments.client.widget.menu.MainMenu;
import org.jboss.errai.ui.nav.client.local.Navigation;
import org.jboss.errai.ui.shared.api.annotations.DataField;

import com.google.gwt.user.client.ui.Composite;

public class MyDefaultPage extends Composite {
    @Inject
    @DataField
    protected LoginStatus loginStatus;

    @Inject
    @DataField
    private MainMenu menu;

    @Inject
    protected Navigation navigation;
}
