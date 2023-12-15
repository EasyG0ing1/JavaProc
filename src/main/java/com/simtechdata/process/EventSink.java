package com.simtechdata.process;

interface EventSink {
    void dispatch(ExecutionEvent event);
}
