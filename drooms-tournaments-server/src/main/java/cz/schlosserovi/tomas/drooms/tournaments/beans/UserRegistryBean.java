package cz.schlosserovi.tomas.drooms.tournaments.beans;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class UserRegistryBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistryBean.class);
    @Inject
    private GameRegistryBean gameQueue;
    @Inject
    private ScheduledExecutorService scheduler;

    private final Map<UUID, Long> userRegistry = new HashMap<>();

    @PostConstruct
    public void startThread() {
        LOGGER.info("Scheduling executor check-in");
        scheduler.scheduleWithFixedDelay(new TimeoutChecker(), 10L, 10L, TimeUnit.MINUTES);
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
                    LOGGER.warn("Executor {} failed to check-in, unregistering", e.getKey());
                    unregister(e.getKey());
                }
            }
        }
    }
}
