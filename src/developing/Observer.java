package developing;

import core.Observer;
import core.SchedulerWorker;

/**
 * Observer 的装饰者
 * <p>
 * 加强功能：能够线程切换
 *  因为线程是外部指定的，不能写死，所以需要外部传入一个能够执行runnable的东东[SchedulerWorker]
 * @param <T> 数据类型
 */
class ObserveOnObserver5<T> implements Observer<T> {

    private final SchedulerWorker worker;
    private final Observer<T> observer;

    public ObserveOnObserver5(Observer<T> observer, SchedulerWorker worker) {
        this.worker = worker;
        this.observer = observer;
    }

    @Override
    public void onSubscribe() {
        worker.run(observer::onSubscribe);
    }

    @Override
    public void onNext(T data) {
        worker.run(() -> observer.onNext(data));
    }

    @Override
    public void onError(Throwable e) {
        worker.run(() -> observer.onError(e));
    }

    @Override
    public void onComplete() {
        worker.run(observer::onComplete);
    }
}
