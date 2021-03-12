package developing;


public class DemoRun {

    public static String currThreadName() {
        return " [thread: " + Thread.currentThread().getName() + "] ";
    }

    public static void printlnData(String data) {
        String res = " [data: " + data + "] ";
        System.out.println(res);
    }

    public static void main(String[] args) {

        new ObservableSource2<String>() {
            @Override
            public void subscribe(Observer2<String> observer) {
                observer.onSubscribe();
                //上面代表回调已经设置好了，这个事情只需要告知一次即可

                // 假设这里我不发射数据，或者。。我等一会再调用其他接口是不是也可以
                // sleep(5000)
                observer.onNext("hello");
                observer.onNext("world");

                // sleep(5000)
                observer.onComplete();
                // 所以说 onNext，onError，onComplete  做的事情不太一样，是发射数据，可以单独弄一个类去做，但这个类肯定也要持有observer，不然没法回调
                // 但是，如果持有observer的话，这个类不守规矩，又去调用了onSubscribe，咋办？---》接口隔离，已经调用的去掉
            }
        }.subscribe(new Observer2<String>() {
            @Override
            public void onSubscribe() { printlnData("onSubscribe"); }
            @Override
            public void onNext(String data) { printlnData(data);}
            @Override
            public void onError(Throwable e) { }
            @Override
            public void onComplete() { printlnData("onComplete"); }

        });

        /**
         *  打印结果
         *
         *  [data: onSubscribe]
         *  [data: hello]
         *  [data: world]
         *  [data: onComplete]
         */
    }
}
