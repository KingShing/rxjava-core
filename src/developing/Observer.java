package developing;

/**
 * 观察者
 *
 * @param <T> 数据类型
 */
interface Observer1<T> {
    void onNext(T data);
}

interface Observer2<T> {

    // 当使用这new 一个Observer2，传给ObservableSource被观察者时，触发
    // 意思就是回调已经设置好了，现在可以调用onNext了
    void onSubscribe();

    void onNext(T data);

    // 错误的情况也需要
    void onError(Throwable e);

    // 多次回调时，用于确定啥时候onNext调完了，不会再发射数据了
    void onComplete();
}

interface Observer3<T> extends Observer2<T> { }
