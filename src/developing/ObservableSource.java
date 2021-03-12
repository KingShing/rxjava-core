package developing;


import core.Observable;
import core.ObservableSource;
import core.Observer;
import core.SchedulerWorker;

/**
 * 被观察者
 *
 * @param <T>
 */
abstract class Observable5<T> implements ObservableSource<T> {



    // 切换下游线程
    public Observable5<T> observeOn(SchedulerWorker worker) {
        return new ObservableObserveOn<T>(this, worker);
    }

    // 增强Observable5 支持传入 线程
    static class ObservableObserveOn<T> extends Observable5<T> {

        private final Observable5<T> source;
        private final SchedulerWorker worker;

        public ObservableObserveOn(Observable5<T> source, SchedulerWorker worker) {
            this.source = source;
            this.worker = worker;
        }

        @Override
        public void subscribe(Observer<T> observer) {
            // new ObserveOnObserver5<>(observer, worker) 下面这里就使用上了增强的观察者
            source.subscribe(new ObserveOnObserver5<>(observer, worker));
        }
    }

    static <U> Observable5<U> create(ObservableOnSubscribe<U> source) {
        return new SimpleObservable<>(source);
    }


    static class SimpleObservable<T> extends Observable5<T> {

        ObservableOnSubscribe<T> source;

        public SimpleObservable(ObservableOnSubscribe<T> source) {
            this.source = source;
        }

        @Override
        public void subscribe(Observer<T> observer) {
            // 下面这2步 内部自己调用，不需要暴露给框架的使用者
            SimpleEmitter<T> emitter = new SimpleEmitter<>(observer);
            observer.onSubscribe();

            source._subscribe(emitter);
        }
    }
}

interface ObservableOnSubscribe<T> {
    // 把 emitter 提供给调用者就行
    void _subscribe(Emitter<T> emitter);
}
