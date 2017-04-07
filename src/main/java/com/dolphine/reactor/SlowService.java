package com.dolphine.reactor;

import org.apache.commons.lang3.RandomStringUtils;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by Tiago Dolphine (tiago.dolphine@ifood.com.br) on 05/04/17.
 */
public class SlowService {

    public String operation1() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello";
    }

    public Mono<String> operation1Reactive() {
        return Mono.create(emitter -> {
            emitter.success(operation1());
        });
    }

    public String operation2() {
        cpuStress();
        return "Tiago";
    }

    public Mono<String> operation2Reactive() {
        return Mono.create(emitter -> {
            emitter.success(operation2());
        });
    }

    public String operation3() {
        cpuStress();
        return "Dolphine";
    }

    public Mono<String> operation3Reactive() {
        return Mono.create(emitter -> {
            emitter.success(operation3());
        });
    }

    private void cpuStress() {
        Collections.sort(IntStream.range(0, 1000000)
                .mapToObj(i -> RandomStringUtils.random(10))
                .collect(Collectors.toList()));
    }

    public String getResponse(String... values) {
        return Stream.of(values).collect(Collectors.joining(" "));
    }
}
