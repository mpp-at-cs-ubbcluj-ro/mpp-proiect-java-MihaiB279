package flight.services;

import flight.model.Flight;

public interface IFlightObserver {
     void ticketsBought(Iterable<Flight> flightd) throws Exception;
}
