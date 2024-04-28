package com.example.skyGoal.configuration;

// Import statements für FootballMatch und WeatherData Klassen

import com.example.skyGoal.entity.FootballMatch;
import com.example.skyGoal.entity.WeatherData;

public class AggregatedMatchWeatherData {
    private FootballMatch match;
    private WeatherData weather;
    private float averageTemperature; // oder andere relevante Wetterdaten, die Sie aggregieren möchten

    // Konstruktor
    public AggregatedMatchWeatherData(FootballMatch match, WeatherData weather) {
        this.match = match;
        this.weather = weather;
        // Berechnen Sie die Durchschnittstemperatur oder andere aggregierte Daten
        this.averageTemperature = weather.getTemperature(); // Beispiel für eine direkte Zuweisung
    }

    // Getter und Setter für Match
    public FootballMatch getMatch() {
        return match;
    }

    public void setMatch(FootballMatch match) {
        this.match = match;
    }

    // Getter und Setter für Weather
    public WeatherData getWeather() {
        return weather;
    }

    public void setWeather(WeatherData weather) {
        this.weather = weather;
    }

    // Getter und Setter für durchschnittliche Temperatur
    public float getAverageTemperature() {
        return averageTemperature;
    }

    public void setAverageTemperature(float averageTemperature) {
        this.averageTemperature = averageTemperature;
    }

    // Überschreiben Sie die toString()-Methode, falls notwendig, um die Daten zu loggen oder zu debuggen
    @Override
    public String toString() {
        return "AggregatedMatchWeatherData{" +
                "match=" + match +
                ", weather=" + weather +
                ", averageTemperature=" + averageTemperature +
                '}';
    }
}
