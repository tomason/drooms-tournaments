package org.drooms.tournaments.client.util;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.drooms.tournaments.client.page.LoginPage;
import org.drooms.tournaments.services.GameService;
import org.drooms.tournaments.services.PlaygroundService;
import org.drooms.tournaments.services.StrategyService;
import org.drooms.tournaments.services.TournamentService;
import org.drooms.tournaments.services.UserService;
import org.jboss.errai.common.client.api.interceptor.InterceptsRemoteCall;
import org.jboss.errai.enterprise.client.jaxrs.api.interceptor.RestCallContext;
import org.jboss.errai.enterprise.client.jaxrs.api.interceptor.RestClientInterceptor;
import org.jboss.errai.ui.nav.client.local.TransitionTo;

import com.google.gwt.http.client.RequestBuilder;

@ApplicationScoped
@InterceptsRemoteCall({ GameService.class, PlaygroundService.class, StrategyService.class, TournamentService.class,
        UserService.class })
public class AuthServiceCallInterceptor implements RestClientInterceptor {
    @Inject
    private ApplicationContext cache;

    @Inject
    private TransitionTo<LoginPage> login;

    @Override
    public void aroundInvoke(RestCallContext context) {
        RequestBuilder builder = context.getRequestBuilder();

        if (builder.getUrl().contains("/auth/")) {
            if (cache.isLoggedIn()) {
                builder.setHeader("Authorization", "BASIC " + cache.getDigest());
            } else {
                login.go();
            }
        }

        context.proceed();
    }
}
