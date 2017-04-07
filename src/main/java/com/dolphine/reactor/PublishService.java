package com.dolphine.reactor;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Tiago Dolphine (tiago.dolphine@ifood.com.br) on 04/04/17.
 */
public class PublishService {

    static EmitterProcessor<String> emitterProcessor = EmitterProcessor.create();

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        //emit values
        emitterProcessor.connect();
        Flux.interval(Duration.ofSeconds(1)).subscribe(t -> emitterProcessor.onNext("Time " + t));

        emitterProcessor.doOnComplete(() -> countDownLatch.countDown())
                .parallel(10)
                .runOn(Schedulers.newParallel("parallel", 10))
                .subscribe(s -> {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(s);
                }, ex -> ex.printStackTrace());
        countDownLatch.await();
    }
}
