package org.drooms.tournaments.server.test;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public final class Deployments {
    private Deployments() {
    }

    /**
     * Default deployment does not contain web.xml (that means no REST)
     */
    public static WebArchive defaultDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class);
        war.addPackages(true, "org.drooms.tournaments");
        war.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        war.addAsWebInfResource("test-persistence.xml", "classes/META-INF/persistence.xml");

        return war;
    }

}
