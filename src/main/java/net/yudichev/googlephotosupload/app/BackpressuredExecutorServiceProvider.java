package net.yudichev.googlephotosupload.app;

import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.yudichev.jiotty.common.inject.BaseLifecycleComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkState;

final class BackpressuredExecutorServiceProvider extends BaseLifecycleComponent implements Provider<ExecutorService> {
    private static final Logger logger = LoggerFactory.getLogger(BackpressuredExecutorServiceProvider.class);
    private ThreadPoolExecutor executor;

    @Override
    public ExecutorService get() {
        checkStarted();
        checkState(executor == null);
        int threadCount = Runtime.getRuntime().availableProcessors() * 2;
        executor = new ThreadPoolExecutor(
                threadCount,
                threadCount,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(threadCount * 2),
                new ThreadFactoryBuilder()
                        .setNameFormat("upload-pool-%s")
                        .setDaemon(true)
                        .build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    @Override
    protected void doStop() {
        if (executor != null) {
            if (!MoreExecutors.shutdownAndAwaitTermination(executor, 15, TimeUnit.SECONDS)) {
                logger.warn("Failed to shutdown upload thread pool in 15 seconds");
            }
            executor = null;
        }
    }
}
