package flight.rest.client;

import flight.model.Flight;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

public class FlightClient {
    public static final String URL = "http://localhost:8080/agency/flights";
    private RestTemplate restTemplate = new RestTemplate();

    public FlightClient() {
    }

    private <T> T execute(Callable<T> callable) throws Exception {
        try {
            return callable.call();
        } catch (Exception var3) {
            throw new Exception(var3);
        }
    }

    public Iterable<Flight> getAll() {
        ResponseEntity<Iterable<Flight>> responseEntity = restTemplate.exchange(URL, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
        return responseEntity.getBody();
    }

    public Flight getById(String id) throws Exception {
        return this.execute(() -> this.restTemplate.getForObject(String.format("%s/%s", URL, id), Flight.class, new Object[0]));
    }

    public Flight create(Flight flight) throws Exception {
        return this.execute(() -> this.restTemplate.postForObject(URL, flight, Flight.class, new Object[0]));
    }

    public void update(int id, Flight flight) throws Exception {
        this.execute(() -> {
            this.restTemplate.put(String.format("%s/%s", URL, id), flight, new Object[0]);
            return null;
        });
    }

    public void delete(String id) throws Exception {
        this.execute(() -> {
            this.restTemplate.delete(String.format("%s/%s", URL, id), new Object[0]);
            return null;
        });
    }
}
