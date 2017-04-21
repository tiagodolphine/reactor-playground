package com.dolphine.reactor;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Tiago Dolphine (tiago.dolphine@ifood.com.br) on 04/04/17.
 */
public class PublishExample {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        //publisher values
        EmitterProcessor<Long> emitterProcessor = EmitterProcessor.create();

        //publisher by time (simulating events being received)
        Flux.interval(Duration.ofSeconds(1)).subscribe(t -> emitterProcessor.onNext(t));

        //publishing events to be handled
        emitterProcessor.connect()
                .doOnComplete(() -> countDownLatch.countDown())
                .parallel(2)
                .runOn(Schedulers.parallel())
                //.filter(t -> t % 2 == 0)
                .map(s -> "Received Second " + s)
                .subscribe(s -> {
                    //save on database or send a request....
                    System.out.println(s);
                }, ex -> ex.printStackTrace());

        System.out.println("Start...");
        countDownLatch.await();
    }
}
