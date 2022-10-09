import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class Client {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String nameSensor = "Sensor1";
//        registrationSensor(nameSensor, restTemplate);
        Random random = new Random();
        Double maxTemperature = 55.0;
        Double minTemperature = -60.0;
        for(int i =0; i <1000; i++){
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
}
