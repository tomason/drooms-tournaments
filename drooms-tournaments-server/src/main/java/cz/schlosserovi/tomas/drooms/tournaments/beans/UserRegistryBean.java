package cz.schlosserovi.tomas.drooms.tournaments.beans;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class UserRegistryBean {
    private static final Logger logger = LoggerFactory.getLogger(UserRegistryBean.class);
    @Inject
    private GameRegistryBean gameQueue;

    private final Map<UUID, Long> userRegistry = new HashMap<>();
    private final ScheduledExecutorService thread = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    public void startThread() {
        logger.info("Scheduling executor check-in");
        thread.scheduleWithFixedDelay(new TimeoutChecker(), 0, 10, TimeUnit.MINUTES);
    }

    @PreDestroy
    public void stopThread() {
        logger.info("Shutting down executor check-in thread");
        thread.shutdown();
    }

    public UUID register() {
        UUID id = UUID.randomUUID();
        userRegistry.put(id, getTime());

        return id;
    }

    private boolean unregister(UUID id) {
        gameQueue.freeGames(id);
        return userRegistry.remove(id) != null;
    }

    public void ping(UUID id) {
        if (userRegistry.containsKey(id)) {
            userRegistry.put(id, getTime());
        } else {
            throw new IllegalArgumentException(String.format("User with id '%s' is not registered", id));
        }
    }

    private long getTime() {
        return System.currentTimeMillis();
    }

    private final class TimeoutChecker implements Runnable {
        // timeout is 10 minutes
        private static final long TIMEOUT = 600_000L;

        @Override
        public void run() {
            long currentTime = getTime();
            for (Entry<UUID, Long> e : userRegistry.entrySet()) {
                if (currentTime - e.getValue() > TIMEOUT) {
                    logger.warn("Executor {} failed to check-in, unregistering", e.getKey());
                    unregister(e.getKey());
                }
            }
        }
    }
}
