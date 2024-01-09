package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
public class Main {
    private static final List<LocationDimension> locations = List.of(
            new LocationDimension(1, 35.6762, 139.6503, "Tokyo"),
            new LocationDimension(2, 49.9935, 36.2304, "Kharkiv"),
            new LocationDimension(3, 51.5074, -0.1278, "London"),
            new LocationDimension(4, 40.7128, -74.0060, "New York"),
            new LocationDimension(5, 52.5200, 13.4050, "Berlin"),
            new LocationDimension(6, 50.45, 30.523, "Kyiv"),
            new LocationDimension(7, -35.2820, 149.1287, "Canberra"),
            new LocationDimension(8, 39.9042, 116.4074, "Beijing"),
            new LocationDimension(9, 45.4, -75.68, "Ottawa"),
            new LocationDimension(10, 48.8566, 2.3522, "Paris"),
            new LocationDimension(11, 46.4825, 30.7233, "Odesa"),
            new LocationDimension(12, 52.2297, 21.0122, "Warsaw"),
            new LocationDimension(13, 41.9028, 12.4964, "Rome"),
            new LocationDimension(14, 55.6761, 12.5683, "Copenhagen"),
            new LocationDimension(15, 59.3293, 18.0686, "Stockholm"),
            new LocationDimension(16, 6.5244, 3.3792, "Lagos"),
            new LocationDimension(17, 59.9139, 10.7522, "Oslo"),
            new LocationDimension(18, 52.3702, 4.8952, "Amsterdam"),
            new LocationDimension(19, 48.4205, 6.6897, "Manila"),
            new LocationDimension(20, 40.5021, 85.6523, "Astana")
    );

    public static void main(String[] args) {
        List<String> citiesWithTemperatureIncrease = new ArrayList<>();
        List<String> citiesWithConsecutiveDaysPrecipitation = new ArrayList<>();

        Map<String, Double> monthlyTemperatureTotals = new HashMap<>();
        Map<String, Integer> monthlyTemperatureCounts = new HashMap<>();

        Map<String, Double> monthlyHumidityTotals = new HashMap<>();
        Map<String, Integer> monthlyHumidityCounts = new HashMap<>();

        Map<String, Double> monthlyPrecipitationTotals = new HashMap<>();
        Map<String, Integer> monthlyPrecipitationCounts = new HashMap<>();

        Map<String, Double> monthlyWindSpeedTotals = new HashMap<>();
        Map<String, Integer> monthlyWindSpeedCounts = new HashMap<>();

        for (LocationDimension location : locations) {
            try {
                WeatherApiInfo apiFetcher = new WeatherApiInfo();
                String apiUrl = apiFetcher.getApiForCity(location);
                String responseData = apiFetcher.HttpRequest(apiUrl);

                WeatherJSONMapper apiMapper = new WeatherJSONMapper();
                apiMapper.processWeatherData(location.getCity(), responseData);

                double meanTemperature = apiMapper.getMeanTemperature();
                double averageHumidity = apiMapper.getAverageHumid();

                location.setMeanTemperature(meanTemperature);
                location.setAverageHumid(averageHumidity);

                double[] temperatureValues = apiMapper.getTemperatureValues();
                if (LimitCalculation.temperatureIncrease(temperatureValues, 5, 5)) {
                    citiesWithTemperatureIncrease.add(location.getCity());
                }

                double[] windSpeedValues = apiMapper.getWindSpeedValues();

                double[] precipitationValues = apiMapper.getPrecipitationValues();
                if (LimitCalculation.consecutiveDaysPrecipitation(precipitationValues, 7)) {
                    citiesWithConsecutiveDaysPrecipitation.add(location.getCity());
                }


                for (int i = 0; i < temperatureValues.length; i++) {
                    LocalDate date = location.getStartDate().plusDays(i);
                    String monthKey = date.format(DateTimeFormatter.ofPattern("MMM"));

                    monthlyTemperatureTotals.merge(monthKey, temperatureValues[i], Double::sum);
                    monthlyTemperatureCounts.merge(monthKey, 1, Integer::sum);
                }


                double[] humidityValues = apiMapper.getDailyAverageHumidArray();
                for (int i = 0; i < humidityValues.length; i++) {
                    LocalDate date = location.getStartDate().plusDays(i);
                    String monthKey = date.format(DateTimeFormatter.ofPattern("MMM"));

                    monthlyHumidityTotals.merge(monthKey, humidityValues[i], Double::sum);
                    monthlyHumidityCounts.merge(monthKey, 1, Integer::sum);
                }

                location.setMeanTemperature(meanTemperature);
                location.setAverageHumid(averageHumidity);

                for (int i = 0; i < precipitationValues.length; i++) {
                    LocalDate date = location.getStartDate().plusDays(i);
                    String monthKey = date.format(DateTimeFormatter.ofPattern("MMM"));

                    monthlyPrecipitationTotals.merge(monthKey, precipitationValues[i], Double::sum);
                    monthlyPrecipitationCounts.merge(monthKey, 1, Integer::sum);
                }


                for (int i = 0; i < windSpeedValues.length; i++) {
                    LocalDate date = location.getStartDate().plusDays(i);
                    String monthKey = date.format(DateTimeFormatter.ofPattern("MMM"));

                    monthlyWindSpeedTotals.merge(monthKey, windSpeedValues[i], Double::sum);
                    monthlyWindSpeedCounts.merge(monthKey, 1, Integer::sum);
                }

                processMonthlyData(location, apiMapper.getTemperatureValues(), monthlyTemperatureTotals, monthlyTemperatureCounts);
                processMonthlyData(location, apiMapper.getDailyAverageHumidArray(), monthlyHumidityTotals, monthlyHumidityCounts);
                processMonthlyData(location, apiMapper.getPrecipitationValues(), monthlyPrecipitationTotals, monthlyPrecipitationCounts);
                processMonthlyData(location, apiMapper.getWindSpeedValues(), monthlyWindSpeedTotals, monthlyWindSpeedCounts);
            } catch (Exception e) {
                System.err.println("Error processing weather data for " + location.getCity() + ": " + e.getMessage());
            }
        }

        List<LocationDimension> hottestStations = locations.stream()
                .sorted(Comparator.comparingDouble(location -> -location.getMeanTemperature()))
                .limit(10)
                .toList();

        List<LocationDimension> coldestStations = locations.stream()
                .sorted(Comparator.comparingDouble(LocationDimension::getMeanTemperature))
                .limit(10)
                .collect(Collectors.toList());

        List<LocationDimension> mostHumidStations = locations.stream()
                .sorted(Comparator.comparingDouble(LocationDimension::getAverageHumidity).reversed())
                .limit(10)
                .collect(Collectors.toList());


        Graph.generateGraph("10 hottest stations:", generateTopStationsData(hottestStations));
        Graph.generateGraph("10 coldest stations:", generateTopStationsData(coldestStations));
        Graph.generateGraph("10 humidest stations:", generateHumidStationsData(mostHumidStations));

        if (!citiesWithConsecutiveDaysPrecipitation.isEmpty()) {
            System.out.println("\nPrecipitation for 7 days was in stations: " +
                    String.join(", ", citiesWithConsecutiveDaysPrecipitation) + "\n");
        }else{
            System.out.println("\nPrecipitation for 7 days was in: 0 stations\n");
        }

        if (!citiesWithTemperatureIncrease.isEmpty()) {
            System.out.println("\nTemperature increased by 5°C over 5 days in: " +
                    String.join(", ", citiesWithTemperatureIncrease) + "\n");
        }else{
            System.out.println("\nTemperature increased by 5°C over 5 days in: 0 stations\n");
        }

        Graph.generateGraph("Average Global Temperature for Each Month:", monthlyTemperatureTotals);
        Graph.generateGraph("Average Global Humidity for Each Month:", monthlyHumidityTotals);
        Graph.generateGraph("Average Global Precipitation for Each Month:", monthlyPrecipitationTotals);

        System.out.println("\nMonth with the highest wind speed:");

        String monthWithHighestWindSpeed = LimitCalculation.findMonthWithHighestAverage(monthlyWindSpeedTotals, monthlyWindSpeedCounts);
        double highestAverageWindSpeed = LimitCalculation.calculateAverage(monthlyWindSpeedTotals, monthlyWindSpeedCounts, monthWithHighestWindSpeed);

        System.out.printf("%s  %.2f km per hour%n", monthWithHighestWindSpeed, highestAverageWindSpeed);
    }

    private static void processMonthlyData(LocationDimension location, double[] values, Map<String, Double> totals, Map<String, Integer> counts) {
        for (int i = 0; i < values.length; i++) {
            LocalDate date = location.getStartDate().plusDays(i);
            String monthKey = date.format(DateTimeFormatter.ofPattern("MMM"));

            totals.merge(monthKey, values[i], Double::sum);
            counts.merge(monthKey, 1, Integer::sum);
        }
    }

    private static Map<String, Double> generateTopStationsData(List<LocationDimension> stations) {
        return stations.stream().collect(Collectors.toMap(LocationDimension::getCity, LocationDimension::getMeanTemperature));
    }

    private static Map<String, Double> generateHumidStationsData(List<LocationDimension> stations) {
        return stations.stream().collect(Collectors.toMap(LocationDimension::getCity, LocationDimension::getAverageHumidity));
    }
}