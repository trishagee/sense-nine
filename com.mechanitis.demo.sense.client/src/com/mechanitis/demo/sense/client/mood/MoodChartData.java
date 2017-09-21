package com.mechanitis.demo.sense.client.mood;

import com.mechanitis.demo.sense.service.MessageListener;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import static javafx.collections.FXCollections.observableArrayList;

public class MoodChartData implements MessageListener {
    private final PieChart.Data sadPortion = new PieChart.Data("Sad", 0);
    private final PieChart.Data happyPortion = new PieChart.Data("Happy", 0);
    private final PieChart.Data confusedPortion = new PieChart.Data("Errr...", 0);
    private final ObservableList<PieChart.Data> pieChartData = observableArrayList(sadPortion, happyPortion, confusedPortion);

    ObservableList<PieChart.Data> getPieChartData() {
        return pieChartData;
    }

    public void onMessage(String mood) {
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

    private void incrementPie(PieChart.Data portion) {
        portion.setPieValue(portion.getPieValue() + 1);
    }

}
