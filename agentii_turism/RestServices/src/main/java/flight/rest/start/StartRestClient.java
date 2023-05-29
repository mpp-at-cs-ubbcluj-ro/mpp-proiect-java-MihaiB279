package flight.rest.start;

import flight.model.Flight;
import flight.rest.client.FlightClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

public class StartRestClient {
    private static final FlightClient flightsClient = new FlightClient();

    public StartRestClient() {
    }

    public static void main(String[] args) {
        new RestTemplate();
        Flight flightT = new Flight("Bucuresti", LocalDateTime.now(), "Otopeni", 130);

        try {
            show(() -> {
                try {
                    System.out.println(flightsClient.create(flightT));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            show(() -> {
                Iterable<Flight> res = null;
                try {
                    res = flightsClient.getAll();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                for (Flight u : res) {
                    System.out.println(u.getId() + ": " + u.getDestination() + " " + u.getDepartureDateTime() + " " +
                            u.getAirport() + " " + u.getAvailableSeats());
                }

            });
            show(() -> {
                Flight flightR = new Flight("Olanda", LocalDateTime.now(), "AirportO", 130);
                Flight flightres;
                try {
                    flightsClient.update(3, flightR);
                    flightres = flightsClient.getById("3");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                System.out.println(flightres.getId() + ": " + flightres.getDestination() + " " +
                        flightres.getDepartureDateTime() + " " + flightres.getAirport() + " " + flightres.getAvailableSeats());
            });
            show(() -> {
                Iterable<Flight> res = null;
                try {
                    flightsClient.delete("3");
                    res = flightsClient.getAll();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                for (Flight u : res) {
                    System.out.println(u.getId() + ": " + u.getDestination() + " " + u.getDepartureDateTime() + " " +
                            u.getAirport() + " " + u.getAvailableSeats());
                }
            });
        } catch (RestClientException var4) {
            System.out.println("Exception ... " + var4.getMessage());
        }

    }

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (Exception var2) {
            System.out.println("Service exception" + var2);
        }

    }
}
