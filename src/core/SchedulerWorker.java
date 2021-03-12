package core;

public interface SchedulerWorker {

    void run(Runnable run);

    static <T> ObservableTransformer<T, T> ioMain() {
        return upstream ->
                upstream.subscribeOn(SchedulerWorker::workerThread)
                        .observeOn(SchedulerWorker::mockUiThread);
    }

    static void workerThread(Runnable runnable) {
        new Thread(runnable, "worker thread").start();
    }

    static SchedulerWorker work() {
        return new SchedulerWorker() {
            @Override
            public void run(Runnable run) {
                new Thread(run, "worker thread:" + this.hashCode()).start();
            }
        };
    }

    static SchedulerWorker ui() {
        return new SchedulerWorker() {
            @Override
            public void run(Runnable run) {
                new Thread(run, "ui thread:" + this.hashCode()).start();
            }
        };
    }

    static void mockUiThread(Runnable runnable) {
        new Thread(runnable, "ui thread").start();
    }
}
