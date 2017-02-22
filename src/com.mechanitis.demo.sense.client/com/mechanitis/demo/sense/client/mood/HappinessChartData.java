package com.mechanitis.demo.sense.client.mood;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Flow;

import static java.time.LocalTime.now;

public class HappinessChartData implements Flow.Subscriber<TweetMood> {
    private final XYChart.Series<String, Double> dataSeries = new XYChart.Series<>();
    private final Map<Integer, Integer> minuteToDataPosition = new HashMap<>();

    public HappinessChartData() {
        // TODO: get minute value for right now

        // TODO: create an empty bar for every minute for the next ten minutes
    }

    private void initialiseBarToZero(int minute) {
        dataSeries.getData().add(new Data<>(String.valueOf(minute), 0.0));
        minuteToDataPosition.put(minute, dataSeries.getData().size() - 1);
    }

    @Override
    public void onNext(TweetMood message) {
        if (message.isHappy()) {
            int x = now().getMinute();

            Integer dataIndex = minuteToDataPosition.get(x);
            Data<String, Double> barForNow = dataSeries.getData().get(dataIndex);
            barForNow.setYValue(barForNow.getYValue() + 1);
        }
    }

    XYChart.Series<String, Double> getDataSeries() {
        return dataSeries;
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

