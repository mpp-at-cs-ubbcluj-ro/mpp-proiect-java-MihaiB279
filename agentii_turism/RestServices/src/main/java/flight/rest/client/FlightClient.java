package flight.rest.client;

import flight.model.Flight;
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
        } catch (HttpClientErrorException | ResourceAccessException var3) {
            throw new Exception(var3);
        } catch (Exception var4) {
            throw new Exception(var4);
        }
    }

    public Iterable<Flight> getAll() throws Exception {
        return (Iterable<Flight> )this.execute(() -> {
            return (Iterable<Flight> )this.restTemplate.getForObject("http://localhost:8080/chat/users", Iterable.class, new Object[0]);
        });
    }

    public Flight getById(String id) throws Exception {
        return (Flight)this.execute(() -> {
            return (Flight)this.restTemplate.getForObject(String.format("%s/%s", "http://localhost:8080/chat/users", id), Flight.class, new Object[0]);
        });
    }

    public Flight create(Flight user) throws Exception {
        return (Flight)this.execute(() -> {
            return (Flight)this.restTemplate.postForObject("http://localhost:8080/chat/users", user, Flight.class, new Object[0]);
        });
    }

    public void update(Flight user) throws Exception {
        this.execute(() -> {
            this.restTemplate.put(String.format("%s/%s", "http://localhost:8080/chat/users", user.getId()), user, new Object[0]);
            return null;
        });
    }

    public void delete(String id) throws Exception {
        this.execute(() -> {
            this.restTemplate.delete(String.format("%s/%s", "http://localhost:8080/chat/users", id), new Object[0]);
            return null;
        });
    }
}
