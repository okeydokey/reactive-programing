package toby;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yhkim on 2017-02-23.
 */
public class ObservableTest {
    // complete, error 처리가 없음.

    static class IntObservable extends Observable implements Runnable {

        @Override
        public void run() {
            for(int i = 1; i <= 10; i++) {
                setChanged();
                notifyObservers(i);
            }
        }
    }

    public static void main(String[] args) {

        Observer ob = (o, arg) -> System.out.println(Thread.currentThread().getName() + " " + arg);

        IntObservable iob = new IntObservable();
        iob.addObserver(ob);

//        iob.run();
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(iob);

        System.out.println(Thread.currentThread().getName() + " END");

        es.shutdown();
    }
}
