package cz.schlosserovi.tomas.drooms.tournaments.beans;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Startup
public class SchedulerBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerBean.class);
    private final ScheduledExecutorService thread = Executors.newScheduledThreadPool(3);

    @PreDestroy
    public void stopThread() {
        LOGGER.info("Shutting down scheduler service");
        thread.shutdown();
    }

    @Produces
    public ScheduledExecutorService getScheduler() {
        return thread;
    }

}
