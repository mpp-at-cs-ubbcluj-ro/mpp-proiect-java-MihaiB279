package flight.networking.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FlightDetailsDTO implements Serializable {
    private String destination;
    private LocalDateTime departureDateTime;

    public FlightDetailsDTO(String dest, LocalDateTime date) {
        this.destination = dest;
        this.departureDateTime = date;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    @Override
    public String toString() {
        return "FlightDTO[" + destination + ' ' + departureDateTime + ' ' + "]";
    }
}
