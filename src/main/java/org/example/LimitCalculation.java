package org.example;

import java.util.*;
import java.util.stream.IntStream;
public class LimitCalculation {
    public static final int hours_per_day = 24;

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static double[] calculateDailyAverageHumidity(double[] hourlyHumidityValues) {
        int numberOfDays = hourlyHumidityValues.length / hours_per_day;

        return IntStream.range(0, numberOfDays)
                .mapToDouble(dayIndex -> {
                    int startIndex = dayIndex * hours_per_day;
                    int endIndex = (dayIndex + 1) * hours_per_day;
                    return Arrays.stream(hourlyHumidityValues, startIndex, endIndex).average().orElse(0);
                }).map(value -> round(value, 2)).toArray();
    }

    public static double calculateMeanTemperature(double[] temperatureValues) {
        double meanTemperature = Arrays.stream(temperatureValues).average().orElse(0);
        return round(meanTemperature, 2);
    }

    public static double calculateAverageHumid(double[] humidityValues) {
        double averageHumidity = Arrays.stream(humidityValues).average().orElse(0);
        return round(averageHumidity, 2);
    }

    public static boolean temperatureIncrease(double[] temperatureValues, double threshold, int consecutiveDays) {
        return IntStream.range(0, temperatureValues.length - consecutiveDays + 1)
                .anyMatch(i -> {
                    double initialTemperature = temperatureValues[i];
                    return IntStream.range(i + 1, i + consecutiveDays)
                            .allMatch(j -> temperatureValues[j] - initialTemperature >= threshold);
                });
    }

    public static boolean consecutiveDaysPrecipitation(double[] precipitationValues, int consecutiveDays) {
        return IntStream.range(0, precipitationValues.length - consecutiveDays + 1).anyMatch(i -> Arrays.stream(precipitationValues, i, i + consecutiveDays).allMatch(value -> value > 0));
    }

    public static String findMonthWithHighestAverage(Map<String, Double> totals, Map<String, Integer> counts) {
        return totals.entrySet().stream().max(Comparator.comparingDouble(entry -> entry.getValue() / counts.get(entry.getKey()))).map(Map.Entry::getKey).orElse("");
    }

    public static double calculateAverage(Map<String, Double> totals, Map<String, Integer> counts, String month) {
        double total = totals.getOrDefault(month, 0.0);
        int count = counts.getOrDefault(month, 1);
        return total / count;
    }
}