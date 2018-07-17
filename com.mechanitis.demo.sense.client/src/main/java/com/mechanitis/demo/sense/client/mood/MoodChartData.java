package com.mechanitis.demo.sense.client.mood;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.util.concurrent.Flow;

import static javafx.collections.FXCollections.observableArrayList;

public class MoodChartData implements Flow.Subscriber<String> {
    private final PieChart.Data sadPortion = new PieChart.Data("Sad", 0);
    private final PieChart.Data happyPortion = new PieChart.Data("Happy", 0);
    private final PieChart.Data confusedPortion = new PieChart.Data("Errr...", 0);
    private final ObservableList<PieChart.Data> pieChartData = observableArrayList(sadPortion, happyPortion, confusedPortion);

    ObservableList<PieChart.Data> getPieChartData() {
        return pieChartData;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(String mood) {
        if (mood.equals("SAD")) {
            incrementPie(sadPortion);
        }
        if (mood.equals("HAPPY")) {
            incrementPie(happyPortion);
        }
        if (mood.equals("HAPPY,SAD")) {
            incrementPie(confusedPortion);
        }

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }

    private void incrementPie(PieChart.Data portion) {
        portion.setPieValue(portion.getPieValue() + 1);
    }

}
