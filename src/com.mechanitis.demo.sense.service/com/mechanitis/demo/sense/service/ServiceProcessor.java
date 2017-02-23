package com.mechanitis.demo.sense.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Flow;
import java.util.function.Function;

// TODO: erm, almost everything....
public class ServiceProcessor implements Flow.Processor<String, String> {
    private final List<Flow.Subscriber<? super String>> subscribers = new CopyOnWriteArrayList<>();
    private Function<String, String> messageHandler;

    public ServiceProcessor(Function<String, String> messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super String> subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void onNext(String item) {
        String output = messageHandler.apply(item);
        subscribers.forEach(subscriber -> subscriber.onNext(output));
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
