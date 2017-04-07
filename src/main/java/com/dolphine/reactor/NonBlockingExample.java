package com.dolphine.reactor;

import org.apache.commons.lang3.time.StopWatch;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Tiago Dolphine (tiago.dolphine@ifood.com.br) on 05/04/17.
 */
public class NonBlockingExample {

    private SlowService slowService = new SlowService();

    private void runExample() throws InterruptedException {
        String[] response = new String[3];
        CountDownLatch countDownLatch = new CountDownLatch(3);

        slowService.operation1Reactive()
                .subscribeOn(Schedulers.parallel())
                .subscribe(s -> response[0] = s, ex -> ex.printStackTrace(), () -> countDownLatch.countDown());

        slowService.operation2Reactive()
                .subscribeOn(Schedulers.parallel())
                .subscribe(s -> response[1] = s, ex -> ex.printStackTrace(), () -> countDownLatch.countDown());

        slowService.operation3Reactive()
                .subscribeOn(Schedulers.parallel())
                .subscribe(s -> response[2] = s, ex -> ex.printStackTrace(), () -> countDownLatch.countDown());

        countDownLatch.await();

        System.out.println(slowService.getResponse(response));
    }

    public static void main(String[] args) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        NonBlockingExample nonBlockingExample = new NonBlockingExample();
        nonBlockingExample.runExample();

        stopWatch.stop();
        System.out.println(stopWatch.getTime());
    }
}
