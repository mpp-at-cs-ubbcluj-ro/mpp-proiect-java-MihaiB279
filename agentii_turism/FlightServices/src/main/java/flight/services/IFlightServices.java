package flight.services;

import flight.model.Agency;
import flight.model.Flight;

import java.time.LocalDateTime;
import java.util.List;

public interface IFlightServices {
    boolean logIn(String userName, String password, IFlightObserver clientObs) throws Exception;

    void logout(Agency agency) throws Exception;

    Iterable<Flight> findFlights(String destination, LocalDateTime date) throws Exception;

    Iterable<Flight> findAllFlights() throws Exception;

    void buyTickets(Long idFlight, String clientName, String address, List<String> passengers) throws Exception;

}
