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

        new ObservableSource3<String>() {
            @Override
            public void subscribe(Observer3<String> observer) {
                observer.onSubscribe(); // code1
                //上面代表回调已经设置好了，这个事情只需要告知一次即可

                //  数据发射已经给一个类专门去做
                SimpleEmitter<String> simpleEmitter = new SimpleEmitter<>(observer); // code2
                simpleEmitter._onNext("hello world"); // code3
//                simpleEmitter._onError(e); // code4
                simpleEmitter._onComplete(); // code5

                // 但是看起来这么调用有点傻，没啥用啊
                // 实际上我们认真分析，就能发现 code1 code2 都不应该让调用者手动去调用的
                // 只有 code3、4、5 确实需要开发者去调用，所以下一版 将解决上述问题，只暴露必要的接口
            }
        }.subscribe(new Observer3<String>() {
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
         *   [data: onSubscribe]
         *  [data: hello world]
         *  [data: onComplete]
         */
    }
}
