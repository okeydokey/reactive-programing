package toby;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yhkim on 2017-02-24.
 */
public class PubSub {
    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(2);

        Iterator<Integer> it = Arrays.asList(1,2,3,4,5,6,7,8,9,10).iterator();

        Publisher p = (subscriber) -> {
            System.out.println("subscribe");

            subscriber.onSubscribe(new Subscription() {
                public void request(long l) {
                    System.out.println("request");

                    es.execute(() -> {
                        int i = 0;
                        try {
                            while (i++ < l) {
                                if (it.hasNext()) {
                                    subscriber.onNext(it.next());
                                } else {
                                    // 10일때 안불려짐... 맞는가 ?
                                    subscriber.onComplete();
                                    break;
                                }
                            }
                        } catch (RuntimeException e) {
                            subscriber.onError(e);
                        }
                    });
                }

                public void cancel() {
                    System.out.println("cancel");
                }
            });
        };

        Subscriber ss = new Subscriber() {
            Subscription sc;

            public void onSubscribe(Subscription subscription) {
                System.out.println("onSubscribe");
                this.sc = subscription;
                this.sc.request(1);
            }

            public void onNext(Object o) {
                System.out.println("onNext");
                System.out.println(Thread.currentThread().getName() + " : " + o);
                this.sc.request(1);
            }

            public void onError(Throwable throwable) {
                System.out.println("onError");
                es.shutdown();
            }

            public void onComplete() {
                System.out.println("onComplete");
                // 셧다운 한다. 이미 Executor에 제공된 작업은 실행되지만, 새로운 작업은 수용하지 않는다.
                es.shutdown();
            }
        };

        p.subscribe(ss);
    }
}
