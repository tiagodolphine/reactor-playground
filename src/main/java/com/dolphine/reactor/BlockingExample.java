package com.dolphine.reactor;


import org.apache.commons.lang3.time.StopWatch;

/**
 * Created by Tiago Dolphine (tiago.dolphine@ifood.com.br) on 05/04/17.
 */
public class BlockingExample {

    private SlowService slowService = new SlowService();

    private void runExample() {
        String[] response = new String[3];

        response[0] = slowService.operation1();

        response[1] = slowService.operation2();

        response[2] = slowService.operation3();

        System.out.println(slowService.getResponse(response));
    }

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        BlockingExample nonBlockingExample = new BlockingExample();

        nonBlockingExample.runExample();

        stopWatch.stop();
        System.out.println(stopWatch.getTime());
    }
}
