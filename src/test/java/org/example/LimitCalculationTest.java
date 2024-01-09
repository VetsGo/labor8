package org.example;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class LimitCalculationTest {

    @Test
    void calculateDailyAverageHumidity() {
        double[] hourlyHumidityValues = {22, 60, 120, 80, 85, 100, 26, 30, 72, 10, 11, 90};
        double[] expected = {};

        double[] result = LimitCalculation.calculateDailyAverageHumidity(hourlyHumidityValues);

        assertArrayEquals(expected, result, 0.01);
    }

    @Test
    void calculateMeanTemperature() {
        double[] temperatureValues = {30.0, 40.0, 50.0, 60.0, 70.0};
        double expected = 50.0;

        double result = LimitCalculation.calculateMeanTemperature(temperatureValues);

        assertEquals(expected, result, 0.01);
    }

    @Test
    void calculateAverageHumid() {
        double[] humidValues = {50.0, 60.0, 70.0, 80.0, 90.0};
        double expected = 70.0;

        double result = LimitCalculation.calculateAverageHumid(humidValues);

        assertEquals(expected, result, 0.01);
    }

    @Test
    void temperatureIncrease() {
        double[] temperatureValues = {30.0, 40.0, 50.0, 60.0, 70.0};
        double threshold = 10.0;
        int consecutiveDays = 4;

        assertTrue(LimitCalculation.temperatureIncrease(temperatureValues, threshold, consecutiveDays));
    }

    @Test
    void consecutiveDaysPrecipitation() {
        double[] precipitationValues = {0.0, 0.0, 0.0, 0.0, 0.0};
        int consecutiveDays = 4;

        assertFalse(LimitCalculation.consecutiveDaysPrecipitation(precipitationValues, consecutiveDays));
    }

    @Test
    void findMonthWithHighestAverage() {
        Map<String, Double> totals = new HashMap<>();
        totals.put("January", 100.0);
        totals.put("February", 150.0);
        totals.put("March", 120.0);

        Map<String, Integer> counts = new HashMap<>();
        counts.put("January", 10);
        counts.put("February", 5);
        counts.put("March", 8);

        String result = LimitCalculation.findMonthWithHighestAverage(totals, counts);

        assertEquals("February", result);
    }

    @Test
    void calculateAverage() {
        Map<String, Double> totals = new HashMap<>();
        totals.put("January", 100.0);

        Map<String, Integer> counts = new HashMap<>();
        counts.put("January", 10);

        String month = "January";

        double result = LimitCalculation.calculateAverage(totals, counts, month);

        assertEquals(10.0, result, 0.01);
    }
}