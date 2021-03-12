package developing;


/**
 * 被观察者
 *
 * @param <T>
 */
interface ObservableSource1<T> {

    void subscribe(Observer1<T> observer);
}

interface ObservableSource2<T> {

    void subscribe(Observer2<T> observer);
}

interface ObservableSource3<T> {

    void subscribe(Observer3<T> observer);
}

interface ObservableSource4<T> {

    void subscribe(Observer4<T> observer);
}

abstract class Observable4<T> implements ObservableSource4<T> {

    static <U> Observable4<U> create(ObservableOnSubscribe4<U> source) {
        return new SimpleObservable4<>(source);
    }

    static class SimpleObservable4<T> extends Observable4<T> {

        ObservableOnSubscribe4<T> source;

        public SimpleObservable4(ObservableOnSubscribe4<T> source) {
            this.source = source;
        }

        @Override
        public void subscribe(Observer4<T> observer) {
            // 下面这2步 内部自己调用，不需要暴露给框架的使用者
            SimpleEmitter<T> emitter = new SimpleEmitter<>(observer);
            observer.onSubscribe();

            source._subscribe(emitter);
        }
    }
}

interface ObservableOnSubscribe4<T> {
    // 把 emitter 提供给调用者就行
    void _subscribe(Emitter<T> emitter);
}
