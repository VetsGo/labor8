package org.example;

import java.util.*;
import java.util.stream.Collectors;
public class Graph {
    public static void generateGraph(String title, Map<String, Double> data) {
        int defValue = 55;

        System.out.println(title);

        List<Map.Entry<String, Double>> sortedData = data.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toList());

        double maxValue = sortedData.stream().mapToDouble(Map.Entry::getValue).max().orElse(1.0);

        for (Map.Entry<String, Double> entry : sortedData) {
            String label = entry.getKey();
            double value = entry.getValue();
            int scaledValue = (int) (value / maxValue * defValue);

            String bar = "*".repeat(scaledValue);
            System.out.printf("%-10s ↑ %-" + defValue + "s %.2f%n", label, bar, value);
        }
        System.out.println();
    }
}