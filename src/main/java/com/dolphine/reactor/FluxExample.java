package com.dolphine.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Tiago Dolphine (tiago.dolphine@ifood.com.br) on 14/04/17.
 */
public class FluxExample {

    private static final Logger logger = LoggerFactory.getLogger(FluxExample.class);

    private static String[] COLORS = {"red", "white", "blue", "green", "black"};

    public static void parallelFlux() {
        Flux.just(COLORS)
                .log()
                .map(String::toUpperCase)
                .subscribeOn(Schedulers.newParallel("sub"))
                .publishOn(Schedulers.newParallel("pub"), 2)
                .subscribe(value -> {
                    logger.info("Consumed: " + value);
                });
    }

    public static void parallel2() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Flux.just(COLORS)
                .log()
                .flatMap(value -> Mono.just(value.toUpperCase()).subscribeOn(Schedulers.parallel()), 4)
                .doOnComplete(()->latch.countDown())
                .subscribe(value -> {
                    logger.info("Consumed: " + value);
                });

        latch.await();

    }


    public static void main(String[] args) throws InterruptedException {
        parallel2();
    }

}
