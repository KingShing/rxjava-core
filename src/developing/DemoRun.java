package developing;


import core.Observer;
import core.SchedulerWorker;
import core.operator.Function;

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

        // 实现map操作
        // 说白了就是 Observer 回调前 对数据进行一次转换（map）即可
        // 也是增强Observer，传入一个转换规则A, 回调前，利用转换规则，将转换后的数据回调即可

        // 参见 MapObserver

        // 然后 Observable 中提供一个 给用户 输入转化规则A 的方法即可

        Observable5.<String>create(new ObservableOnSubscribe<String>() {
            @Override
            public void _subscribe(Emitter<String> emitter) {
                currThreadName("code1");
                emitter._onNext("123"); // code1

                emitter._onComplete();        // code1
            }
        })
                .map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(String s) throws Exception {
                        return Integer.parseInt(s);
                    }
                })
                .subscribeOn(SchedulerWorker.work())
                .observeOn(SchedulerWorker.ui())
                .subscribe(new Observer<Integer>() {
                    public void onSubscribe() {
                        printlnData("onSubscribe"); // code2
                    }

                    @Override
                    public void onNext(Integer data) {
                        printlnData(++data + "");  // code2
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        printlnData("onComplete"); // code2
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
