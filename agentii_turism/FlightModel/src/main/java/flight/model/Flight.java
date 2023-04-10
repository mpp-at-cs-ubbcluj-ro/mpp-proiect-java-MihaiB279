package flight.model;

import java.time.LocalDateTime;

public class Flight extends Entity<Long> {

    private String destination;
    private LocalDateTime departureDateTime;
    private String airport;
    private int availableSeats;

    public Flight(String dest, LocalDateTime date, String airport, int seats) {
        this.destination = dest;
        this.departureDateTime = date;
        this.airport = airport;
        this.availableSeats = seats;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

}
