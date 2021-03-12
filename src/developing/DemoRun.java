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

        Observable4.<String>create(new ObservableOnSubscribe4<String>(){
            @Override
            public void _subscribe(Emitter<String> emitter) {
                emitter._onNext("hihi");
                emitter._onNext("www");
                emitter._onNext("ww");
                emitter._onComplete();
            }
        }).subscribe(new Observer4<String>() {
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
         *  [data: hihi]
         *  [data: www]
         *  [data: ww]
         *  [data: onComplete]
         */
    }
}
