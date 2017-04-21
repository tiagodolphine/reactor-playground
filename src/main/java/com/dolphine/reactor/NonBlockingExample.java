package com.dolphine.reactor;

import org.apache.commons.lang3.time.StopWatch;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * Created by Tiago Dolphine (tiago.dolphine@ifood.com.br) on 05/04/17.
 */
public class NonBlockingExample {

    private SlowService slowService = new SlowService();

    private void runExample() throws InterruptedException {
        String[] response = new String[3];
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Mono.when(slowService.operation1Reactive().subscribeOn(Schedulers.parallel()),
                slowService.operation2Reactive().subscribeOn(Schedulers.parallel()),
                slowService.operation3Reactive().subscribeOn(Schedulers.parallel()))
                .doFinally(s -> countDownLatch.countDown())
                .doOnSuccess(result -> {
                    response[0] = result.getT1();
                    response[1] = result.getT2();
                    response[2] = result.getT3();
                    countDownLatch.countDown();
                })
                .subscribe();

        countDownLatch.await();

        System.out.println(slowService.getResponse(response));
    }

    private void runExampleCompletableFuture() throws InterruptedException, ExecutionException {
        String[] response = new String[3];
        CountDownLatch countDownLatch = new CountDownLatch(1);

        CompletableFuture.allOf(CompletableFuture.supplyAsync(() -> slowService.operation1())
                        .thenApply(s -> response[0] = s),
                CompletableFuture.supplyAsync(() -> slowService.operation2()).thenApply(s -> response[1] = s),
                CompletableFuture.supplyAsync(() -> slowService.operation3()).thenApply(s -> response[2] = s)).get();


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
