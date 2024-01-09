package org.example;

import java.time.LocalDate;
public class LocationDimension {
    private static LocalDate start_date = LocalDate.of(2020, 4, 8);
    private int id;
    private double latitude;
    private double longitude;
    private String city;
    private double temperature;
    private double averageHumid;

    public LocationDimension(int id, double latitude, double longitude, String city) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCity() {
        return city;
    }

    public LocalDate getStartDate() {
        return start_date;
    }

    public double getMeanTemperature() {
        return temperature;
    }

    public double getAverageHumidity() {
        return averageHumid;
    }

    public void setMeanTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setAverageHumid(double averageHumid) {
        this.averageHumid = averageHumid;
    }
}