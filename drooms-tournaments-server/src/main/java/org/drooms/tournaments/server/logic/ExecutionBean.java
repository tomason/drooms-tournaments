package org.drooms.tournaments.server.logic;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExecutionBean {
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @PreDestroy
    public void disposeExecutor() {
        executor.shutdown();
    }

    public Future<?> submit(Runnable task) {
        return executor.submit(task);
    }

    public <T> Future<T> submit(Callable<T> task) {
        return executor.submit(task);
    }

}
