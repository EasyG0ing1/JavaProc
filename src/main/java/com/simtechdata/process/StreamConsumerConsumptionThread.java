package com.simtechdata.process;

import java.io.InputStream;

import static com.simtechdata.process.ExecutionEvent.EXCEPTION_IN_STREAM_HANDLING;

/**
 * This class ${end}
 */
class StreamConsumerConsumptionThread implements OutputConsumptionThread {
    private final EventSink eventSink;
    private final StreamConsumer stdout;
    private Thread thread;
    private Throwable throwable;

    public StreamConsumerConsumptionThread(EventSink eventSink, StreamConsumer stdout) {
        this.eventSink = eventSink;
        this.stdout = stdout;
    }

    public void startConsumption(final InputStream inputStream) {
        this.thread = new Thread(() -> {
            try {
                stdout.consume(inputStream);

            } catch (Throwable t) {
                if (!thread.isInterrupted()) {
                    StreamConsumerConsumptionThread.this.throwable = t;
                    eventSink.dispatch(EXCEPTION_IN_STREAM_HANDLING);
                }
            }
        });
        thread.start();
    }

    public void join() throws InterruptedException {
        thread.join();
    }

    public void interrupt() {
        thread.interrupt();
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
