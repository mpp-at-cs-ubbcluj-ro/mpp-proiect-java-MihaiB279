package flight.networking.dto;

import flight.model.Agency;
import flight.model.Flight;
import flight.services.IFlightServices;

import java.time.LocalDateTime;
import java.util.ArrayList;


public class DTOUtils {
   public static Agency getFromDTO(AgencyDTO agdto){
        return new Agency(agdto.getId(), agdto.getName(), agdto.getPassword());
    }

    public static FlightDTO getDTO(
            Flight flight) {
        return new FlightDTO(flight.getId(), flight.getDestination(), flight.getDepartureDateTime(), flight.getAirport(), flight.getAvailableSeats());
    }

    public static AgencyDTO getDTO(Agency agency) {
        return new AgencyDTO(agency.getId(), agency.getName(), agency.getPassword());
    }

    public static FlightDTO[] getDTO(Iterable<Flight> flights) {
        int size = 0, i = 0;
        for (Flight f : flights)
            size++;
        FlightDTO[] flightsDTO = new FlightDTO[size];
        for (Flight flight : flights) {
            flightsDTO[i] = getDTO(flight);
            i++;
        }
        return flightsDTO;
    }

    public static Flight getFromDTO(FlightDTO fdto) {
        Flight flight = new Flight(fdto.getDestination(), fdto.getDepartureDateTime(), fdto.getAirport(), fdto.getAvailableSeats());
        flight.setId(fdto.getId());
        return flight;

    }

    public static Iterable<Flight> getFromDTO(FlightDTO[] flightsDTO) {
        ArrayList<Flight> flights = new ArrayList<>();
        for (FlightDTO flightDTO : flightsDTO) {
            flights.add(getFromDTO(flightDTO));
        }
        return flights;
    }
}
