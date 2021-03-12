package developing;


import core.Observer;
import core.SchedulerWorker;

public class DemoRun {

    public static void currThreadName(String methodTag) {
        String res = methodTag + " : [thread: " + Thread.currentThread().getName() + "] ";
        System.out.println(res);
    }

    public static void printlnData(String data) {
        String res = " [data: " + data + "] ";
        System.out.println(res);
    }

    public static void main(String[] args) {

        // 线程切换效果，code1 内，都在A线程中调用 ， code2 内在B线程中调用
        // 要想做到上述效果
        // code1 情况只需要 让ObservableOnSubscribe的 _subscribe() 方法指定在A线程中执行就行
        // code2 情况只需要 Observer的 四个方法 指定在B 线程中执行就ok

        // 先研究 Observer的 ，就是让Observer 具有线程切换的功能，怎么搞？
        //
        // 在Observer 中传入线程? 把需要执行的方法放到线程的run方法中去执行？但是业务侧如果不需要切换线程呢？加个开关？传个空的线程？
        // 所以这个功能应该是可插拔的，最好对原有代码无侵入

        // ---> 装饰器模式，对Observer装饰 [ObserveOnObserver]

        // 有这里加强版的observer后，只需要在 Observable中提供一个切换下游线程的方法即可

        // 上游切换 ，对ObservableOnSubscribe 的 _subscribe 方法进行切换即可，
        // 同理装饰 Observable 即可，对他进行增强

        Observable5.<String>create(new ObservableOnSubscribe<String>() {
            @Override
            public void _subscribe(Emitter<String> emitter) {
                currThreadName("code1");
                emitter._onNext("hihi"); // code1
                emitter._onNext("www");  // code1
                emitter._onNext("ww");   // code1
                emitter._onComplete();        // code1
            }
        })
                .subscribeOn(SchedulerWorker.work())
                // 下游反复切，最后一次生效
                .observeOn(SchedulerWorker.ui())
                .subscribe(new Observer<String>() {
                    public void onSubscribe() {
                        printlnData("onSubscribe"); // code2
                        currThreadName("code2 ：onSubscribe");
                    }

                    @Override
                    public void onNext(String data) {
                        printlnData(data);  // code2
                        currThreadName("code2 ：onNext");
                    }

                    @Override
                    public void onError(Throwable e) {
                        // code2
                    }

                    @Override
                    public void onComplete() {
                        printlnData("onComplete"); // code2
                        currThreadName("code2 ：onComplete");
                    }
                });


        /**
         *   运行结果：
         *   code1 : [thread: worker thread:1791741888]
         *  [data: onSubscribe]
         * code2 ：onSubscribe : [thread: ui thread:2080602258]
         *  [data: hihi]
         * code2 ：onNext : [thread: ui thread:2080602258]
         *  [data: www]
         * code2 ：onNext : [thread: ui thread:2080602258]
         *  [data: ww]
         * code2 ：onNext : [thread: ui thread:2080602258]
         *  [data: onComplete]
         * code2 ：onComplete : [thread: ui thread:2080602258]
         *
         */
    }
}
