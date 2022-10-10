import dto.MeasurementResponse;
import dto.MeasurementsDTO;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Client {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String nameSensor = "Sensor1";
        registrationSensor(nameSensor, restTemplate);
        Random random = new Random();
        Double maxTemperature = 55.0;
        Double minTemperature = -35.0;
        for(int i =0; i <100; i++){
            addMeasurement(nameSensor,
                    random.nextDouble()*(maxTemperature+Math.abs(minTemperature))
                            -maxTemperature,
                    random.nextBoolean(),
                    restTemplate);
        }

    }

    public static void registrationSensor(String nameSensor, RestTemplate restTemplate) {
        String url = "http://localhost:8080/sensors/registration";
        Map<String, String> jsonToSen = new HashMap<>();
        jsonToSen.put("name", nameSensor);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(jsonToSen);
        String response = restTemplate.postForObject(url, request, String.class);
        System.out.println(response);
        drawXYChart(getTemperature(restTemplate));

    }

    public static void addMeasurement(String sensorName, Double value,
                                      Boolean raining, RestTemplate restTemplate) {
        String url = "http://localhost:8080/measurements/add";
        Map<String, Object> jsonToSen = new HashMap<>();
        jsonToSen.put("sensor", Map.of("name", sensorName));
        jsonToSen.put("value", value);
        jsonToSen.put("raining", raining);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(jsonToSen);
        String response = restTemplate.postForObject(url, request, String.class);
        System.out.println(response);
    }

    public static List<Double> getTemperature(RestTemplate restTemplate){
        String url = "http://localhost:8080/measurements";
        MeasurementResponse jsonResponse = restTemplate.getForObject(url,MeasurementResponse.class);
        if(jsonResponse == null ){
            return Collections.emptyList();
        }
        return jsonResponse.getList().stream().map(MeasurementsDTO::getValue).collect(Collectors.toList());
    }

    private static void drawXYChart(List<Double> temperatures){
        double[] xData = IntStream.range(0, temperatures.size()).asDoubleStream().toArray();
        double[] yData = temperatures.stream().mapToDouble(x -> x).toArray();

        XYChart chart = QuickChart.getChart("Temperatures", "X", "Y", "temperature",
                xData, yData);

        new SwingWrapper(chart).displayChart();
    }

}
