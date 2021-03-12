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

        new ObservableSource1<String>() {
            @Override
            public void subscribe(Observer1<String> observer) {
                observer.onNext("hello");
            }
        }.subscribe(new Observer1<String>() {
            @Override
            public void onNext(String data) {
                printlnData(data); // [data: hello]
            }
        });
    }
}
