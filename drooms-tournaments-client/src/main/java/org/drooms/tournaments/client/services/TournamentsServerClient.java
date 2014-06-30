package org.drooms.tournaments.client.services;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

public class TournamentsServerClient {
    private Map<Class<?>, Object> services = new HashMap<>();
    private String server;
    private final ApacheHttpClient4Executor executor;
    private final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();

    public TournamentsServerClient(String server) {
        if (!server.startsWith("http")) {
            server = "https://" + server;
        }
        this.server = server;

        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[] { new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
            } }, new SecureRandom());
            SSLSocketFactory factory = new SSLSocketFactory(new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }, new X509HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }

                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }

                @Override
                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                @Override
                public void verify(String host, SSLSocket ssl) throws IOException {
                }
            });

            SchemeRegistry registry = SchemeRegistryFactory.createDefault();
            registry.register(new Scheme("https", 443, factory));

            ClientConnectionManager manager = new ThreadSafeClientConnManager(registry);

            DefaultHttpClient client = new DefaultHttpClient(manager);
            client.setCredentialsProvider(credentialsProvider);

            executor = new ApacheHttpClient4Executor(client);
        } catch (KeyManagementException | NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getService(Class<T> clazz) {
        if (!services.containsKey(clazz)) {
            services.put(clazz, ProxyFactory.create(clazz, server, executor));
        }

        return clazz.cast(services.get(clazz));
    }

    public void login(String credentials) {
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(credentials));
    }

    public void login(String username, String password) {
        login(username + ":" + password);
    }

    public void logout() {
        credentialsProvider.clear();
    }

    public boolean isLoggedIn() {
        return credentialsProvider.getCredentials(AuthScope.ANY) != null;
    }

    public String getLoogedInUser() {
        Credentials creds = credentialsProvider.getCredentials(AuthScope.ANY);
        if (creds != null) {
            return creds.getUserPrincipal().getName();
        } else {
            return null;
        }
    }
}
