package com.mechanitis.demo.sense.client.mood;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.util.concurrent.Flow;

import static javafx.collections.FXCollections.observableArrayList;

public class MoodChartData implements Flow.Subscriber<String> {
    private final PieChart.Data sadPortion = new PieChart.Data("Sad", 0);
    private final PieChart.Data happyPortion = new PieChart.Data("Happy", 0);
    private final ObservableList<PieChart.Data> pieChartData = observableArrayList(sadPortion, happyPortion);

    ObservableList<PieChart.Data> getPieChartData() {
        return pieChartData;
    }

    @Override
    public void onNext(String mood) {
        if ("SAD".equals(mood)) {
            incrementPie(sadPortion);
        }
        else if ("HAPPY".equals(mood)) {
            incrementPie(happyPortion);
        }
    }

    private void incrementPie(PieChart.Data portion) {
        portion.setPieValue(portion.getPieValue() + 1);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }

    PieChart.Data getHappyPortion() {
        return happyPortion;
    }

    PieChart.Data getSadPortion() {
        return sadPortion;
    }
}
