package flight.networking.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FlightDTO implements Serializable {
    private Long id;
    private String destination;
    private LocalDateTime departureDateTime;
    private String airport;
    private int availableSeats;

    public FlightDTO(Long id, String dest, LocalDateTime date, String airport, int seats) {
        this.id = id;
        this.destination = dest;
        this.departureDateTime = date;
        this.airport = airport;
        this.availableSeats = seats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString(){
        return "FlightDTO["+id+' '+destination+' '+departureDateTime+' '+availableSeats+"]";
    }
}
