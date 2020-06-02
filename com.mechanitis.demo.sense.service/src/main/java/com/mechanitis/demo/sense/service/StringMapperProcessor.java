package com.mechanitis.demo.sense.service;

import java.util.concurrent.Flow;
import java.util.function.Function;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

class StringMapperProcessor implements Flow.Processor<String, String> {
    private static final Logger LOGGER = getLogger(StringMapperProcessor.class.getName());
    private final Function<String, String> mapper;
    private Flow.Subscriber<? super String> subscriber;

    StringMapperProcessor(Function<String, String> mapper) {
        this.mapper = mapper;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onError(Throwable throwable) {
        //TODO error handling
        throwable.printStackTrace();
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(String item) {
        subscriber.onNext(mapper.apply(item));
    }

    @Override
    public void subscribe(Flow.Subscriber<? super String> subscriber) {
        this.subscriber = subscriber;
    }
}
