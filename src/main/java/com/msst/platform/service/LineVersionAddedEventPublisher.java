package com.msst.platform.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.*;
import java.util.function.Consumer;

@Component
public class LineVersionAddedEventPublisher implements ApplicationListener<LineVersionAddedEvent>, // <1>
    Consumer<FluxSink<LineVersionAddedEvent>> { //<2>

    private final Executor executor;
    private final BlockingQueue<LineVersionAddedEvent> queue =
        new LinkedBlockingQueue<>(); // <3>

    LineVersionAddedEventPublisher(@Qualifier("taskExecutor") Executor executor) {
        this.executor = executor;
    }

    // <4>
    @Override
    public void onApplicationEvent(LineVersionAddedEvent event) {
        this.queue.offer(event);
    }

     @Override
    public void accept(FluxSink<LineVersionAddedEvent> sink) {
        this.executor.execute(() -> {
            while (true)
                try {
                    LineVersionAddedEvent event = queue.take(); // <5>
                    sink.next(event); // <6>
                }
                catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
        });
    }

}
