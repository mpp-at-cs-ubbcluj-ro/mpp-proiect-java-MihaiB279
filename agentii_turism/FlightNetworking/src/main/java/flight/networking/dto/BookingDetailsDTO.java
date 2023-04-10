package flight.networking.dto;

import flight.model.Client;
import flight.model.Flight;

import java.io.Serializable;
import java.util.List;

public class BookingDetailsDTO implements Serializable {
    private Long idFlight;
    private String clientName;
    private List<String> passengers;
    private String address;

    public BookingDetailsDTO(Long flight, String client, List<String> passengers, String address) {
        this.idFlight = flight;
        this.clientName = client;
        this.passengers = passengers;
        this.address = address;
    }

    public Long getIdFlight() {
        return idFlight;
    }

    public void setIdFlight(Long idFlight) {
        this.idFlight = idFlight;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<String> passengers) {
        this.passengers = passengers;
    }
}
