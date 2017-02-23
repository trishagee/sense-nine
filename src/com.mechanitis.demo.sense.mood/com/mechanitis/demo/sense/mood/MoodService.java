package com.mechanitis.demo.sense.mood;

import com.mechanitis.demo.sense.service.Service;
import io.reactivex.Flowable;

import javax.websocket.DeploymentException;
import java.io.IOException;

public class MoodService implements Runnable {
    private static final int PORT = 8082;
    private final Service service;

    public MoodService() {
        service = new Service("ws://localhost:8081/tweets/", "/moods/", PORT);
//        service.addProcessor(fullMessage -> MoodAnalyser.analyseMood(Flowable.just(fullMessage)));
    }

    @Override
    public void run() {
        service.run();
    }

    public static void main(String[] args) throws IOException, DeploymentException {
        new MoodService().run();
    }
}
