package developing;


public interface Emitter<T> {

    void _onNext(T data);

    void _onError(Throwable e);

    void _onComplete();
}

class SimpleEmitter<T> implements Emitter<T> {

    final private Observer3<T> observer;

    public SimpleEmitter(Observer3<T> observer) {
        this.observer = observer;
    }

    @Override
    public void _onNext(T data) {
        observer.onNext(data);
    }

    @Override
    public void _onError(Throwable e) {
        observer.onError(e);
    }

    @Override
    public void _onComplete() {
        observer.onComplete();
    }
}
