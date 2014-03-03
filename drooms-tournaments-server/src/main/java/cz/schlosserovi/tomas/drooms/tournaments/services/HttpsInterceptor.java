package cz.schlosserovi.tomas.drooms.tournaments.services;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;

@Provider
@ServerInterceptor
public class HttpsInterceptor implements PreProcessInterceptor {

    @Override
    public ServerResponse preProcess(HttpRequest request, ResourceMethod method) throws Failure, WebApplicationException {
        if (!UserService.class.isAssignableFrom(method.getResourceClass())) {
            return null;
        }
        String protocol = request.getUri().getRequestUri().toString();
        protocol = protocol.substring(0, protocol.indexOf(':'));
        if (!"https".equalsIgnoreCase(protocol)) {
            return new ServerResponse("Use HTTPS for user modification", 403, new Headers<Object>());
        }

        return null;
    }

}
