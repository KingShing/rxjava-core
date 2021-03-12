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

