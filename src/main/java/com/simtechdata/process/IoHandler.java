package com.simtechdata.process;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

class IoHandler {
    private final OutputConsumptionThread stdout;
    private final OutputConsumptionThread stderr;
    Thread inFeeder;


    IoHandler(InputStream stdin, OutputConsumptionThread stdout, OutputConsumptionThread stderr, Process process) {
        this.stdout = stdout;
        this.stderr = stderr;
        InputStream out = process.getInputStream();
        InputStream err = process.getErrorStream();
        OutputStream in = process.getOutputStream();

        stdout.startConsumption(out);
        stderr.startConsumption(err);
        inFeeder = startConsumption(in, stdin);
    }

    List<Throwable> joinConsumption() throws InterruptedException {
        inFeeder.join();
        stdout.join();
        stderr.join();

        List<Throwable> exceptions = new ArrayList<>();

        if (stdout.getThrowable() != null) {
            exceptions.add(stdout.getThrowable());
        }

        if (stderr.getThrowable() != null) {
            exceptions.add(stderr.getThrowable());
        }

        return exceptions;

    }

    void cancelConsumption() {
        inFeeder.interrupt();
        stdout.interrupt();
        stderr.interrupt();
    }

    Thread startConsumption(OutputStream stdout, InputStream out) {
        Thread consumer;
        consumer = new Thread(new StreamCopyRunner(out, stdout, true));
        consumer.start();
        return consumer;
    }

}
