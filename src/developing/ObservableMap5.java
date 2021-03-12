package developing;

import core.Observer;
import core.operator.Function;

public class ObservableMap5<T, R> extends Observable5<R> {

    private final Observable5<T> source;
    private final Function<? super T, ? extends R> function;

    public ObservableMap5(Observable5<T> source, Function<? super T, ? extends R> function) {
        this.source = source;
        this.function = function;
    }

    @Override
    public void subscribe(Observer<R> observer) {
        source.subscribe(new MapObserver<>(observer, function));
    }

    static class MapObserver<T, R> implements Observer<T> {
        private final Observer<R> observer;

        private final Function<? super T, ? extends R> function;

        public MapObserver(Observer<R> observer, Function<? super T, ? extends R> function) {
            this.observer = observer;
            this.function = function;
        }

        @Override
        public void onSubscribe() {
            observer.onSubscribe();
        }

        @Override
        public void onNext(T data) {
            try {
                R apply = function.apply(data);
                observer.onNext(apply);
            } catch (Exception e) {
                onError(e);
            }
        }

        @Override
        public void onError(Throwable e) {
            observer.onError(e);
        }

        @Override
        public void onComplete() {
            observer.onComplete();
        }
    }
}
