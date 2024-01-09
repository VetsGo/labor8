package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
public class WeatherJSONMapper {
    private Gson gson = new Gson();
    private double temperature;
    private double averageHumid;
    private double[] temperatureValues;
    private double[] precipitationValues;
    private double[] windSpeedValues;
    private double[] dailyAverageHumidArray;

    public void processWeatherData(String city, String responseData) {
        JsonObject jsonResponse = gson.fromJson(responseData, JsonObject.class);

        processPrecipitationData(jsonResponse);
        processWindSpeedData(jsonResponse);
        processHourlyHumidData(jsonResponse);
        processTemperatureData(jsonResponse);
    }

    private void processPrecipitationData(JsonObject jsonResponse) {
        JsonArray precipitationArray = jsonResponse.getAsJsonObject("daily").getAsJsonArray("precipitation_sum");
        precipitationValues = gson.fromJson(precipitationArray, double[].class);
    }

    private void processWindSpeedData(JsonObject jsonResponse) {
        JsonArray windSpeedArray = jsonResponse.getAsJsonObject("daily").getAsJsonArray("wind_speed_10m_max");
        windSpeedValues = gson.fromJson(windSpeedArray, double[].class);
    }

    private void processHourlyHumidData(JsonObject jsonResponse) {
        JsonArray hourlyHumidArray = jsonResponse.getAsJsonObject("hourly").getAsJsonArray("relative_humidity_2m");
        double[] hourlyHumidValues = gson.fromJson(hourlyHumidArray, double[].class);
        dailyAverageHumidArray = LimitCalculation.calculateDailyAverageHumidity(hourlyHumidValues);
        averageHumid = LimitCalculation.calculateAverageHumid(hourlyHumidValues);
    }

    private void processTemperatureData(JsonObject jsonResponse) {
        JsonArray temperatureArray = jsonResponse.getAsJsonObject("daily").getAsJsonArray("temperature_2m_mean");
        temperatureValues = gson.fromJson(temperatureArray, double[].class);
        temperature = LimitCalculation.calculateMeanTemperature(temperatureValues);
    }

    public double getMeanTemperature() {
        return temperature;
    }

    public double getAverageHumid() {
        return averageHumid;
    }

    public double[] getTemperatureValues() {
        return temperatureValues;
    }

    public double[] getWindSpeedValues() {
        return windSpeedValues;
    }

    public double[] getPrecipitationValues() {
        return precipitationValues;
    }

    public double[] getDailyAverageHumidArray() {
        return dailyAverageHumidArray;
    }
}