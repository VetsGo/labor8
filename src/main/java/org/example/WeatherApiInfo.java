package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
public class WeatherApiInfo {
    public String getApiForCity(LocationDimension location) {
        LocalDate endDate = location.getStartDate().plusYears(1);

        return String.format("https://archive-api.open-meteo.com/v1/archive?latitude=%s&longitude=%s" +
                        "&start_date=%s&end_date=%s&hourly=relative_humidity_2m" +
                        "&daily=temperature_2m_mean,precipitation_sum,wind_speed_10m_max&timeformat=unixtime",
                location.getLatitude(), location.getLongitude(), location.getStartDate(), endDate);
    }

    public String HttpRequest(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } catch (IOException e) {
            HttpRequestError(apiUrl, e);
            throw e;
        } finally {
            connection.disconnect();
        }
    }

    private void HttpRequestError(String apiUrl, Exception e) {
        System.err.println("HTTP request error:");
        System.err.println("URL: " + apiUrl);
    }
}