package com.mechanitis.demo.sense.client.mood;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;

public class HappinessController {
    @FXML private BarChart<String, Double> happinessOverTime;

    public void setData(HappinessChartData data) {
        happinessOverTime.getData().add(data.getDataSeries());
    }
}
