package org.drooms.tournaments.client.page;

import javax.inject.Inject;

import org.drooms.tournaments.client.util.ApplicationContext;
import org.drooms.tournaments.client.util.FormMode;
import org.drooms.tournaments.client.widget.user.form.UserForm;
import org.drooms.tournaments.domain.User;
import org.drooms.tournaments.services.UserService;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageShowing;
import org.jboss.errai.ui.nav.client.local.PageState;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

@Page
@Templated("MyDefaultPage.html#root")
public class UserDetailPage extends MyDefaultPage {

    @PageState
    private String userName;

    @Inject
    @DataField
    private UserForm content;

    @Inject
    private ApplicationContext context;

    @Inject
    private Caller<UserService> userService;

    @PageShowing
    public void init() {
        content.setMode(FormMode.DETAIL);

        String name = userName == null || userName.length() == 0 ? context.getUsername() : userName;
        userService.call(new RemoteCallback<User>() {
            @Override
            public void callback(User response) {
                if (response == null) {
                    navigation.goToWithRole(DefaultPage.class);
                } else {
                    content.setValue(response);
                }
            }
        }).getUser(name);
    }
}
