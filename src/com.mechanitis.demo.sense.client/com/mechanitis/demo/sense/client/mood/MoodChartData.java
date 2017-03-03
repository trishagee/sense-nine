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

    @Override
    public void onMessage(String moodAsCsv) {
        TweetMood mood = MoodsParser.parse(moodAsCsv);
        if (mood.isSad()) {
            incrementPie(sadPortion);
        }
        if (mood.isHappy()) {
            incrementPie(happyPortion);
        }
        if (mood.isConfused()) {
            incrementPie(confusedPortion);
        }

    }

    private void incrementPie(PieChart.Data portion) {
        portion.setPieValue(portion.getPieValue() + 1);
    }

}
