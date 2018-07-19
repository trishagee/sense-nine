package com.mechanitis.demo.sense.service.internal;

import java.util.concurrent.Flow;
import java.util.function.Function;

public class StringMapperProcessor implements Flow.Processor<String, String> {
    private final Function<String, String> mapper;
    private Flow.Subscriber<? super String> subscriber;

    public StringMapperProcessor(Function<String, String> mapper) {
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
