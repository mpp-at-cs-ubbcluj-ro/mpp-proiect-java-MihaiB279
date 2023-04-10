package flight.server;


import flight.model.Agency;
import flight.model.Booking;
import flight.model.Client;
import flight.model.Flight;
import flight.persistence.repository.jdbc.AgencyRepository;
import flight.persistence.repository.jdbc.ClientRepository;
import flight.persistence.repository.jdbc.FlightRepository;
import flight.persistence.repository.jdbc.BookingRepository;
import flight.services.IFlightObserver;
import flight.services.IFlightServices;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FlightServiceImpl implements IFlightServices {
    private FlightRepository flightRepository;
    private ClientRepository clientRepository;
    private AgencyRepository agencyRepository;
    private BookingRepository bookingRepository;
    private Map<String, IFlightObserver> loggedClients;
    private final int defaultThreadsNo = 5;

    public FlightServiceImpl(FlightRepository flightRepository, ClientRepository clientRepository, AgencyRepository agencyRepository, BookingRepository bookingRepository) {
        this.flightRepository = flightRepository;
        this.clientRepository = clientRepository;
        this.agencyRepository = agencyRepository;
        this.bookingRepository = bookingRepository;
        loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public boolean logIn(String userName, String password, IFlightObserver clientObs) throws Exception {
        Agency agency = agencyRepository.findOne(userName);
        if (agency == null || !Objects.equals(agency.getPassword(), password)) {
            //throw new Exception("mesaj");
            return false;
        } else {
            if (loggedClients.get(agency.getId()) != null)
                throw new Exception("User already logged in.");
            loggedClients.put(agency.getId(), clientObs);
            return Objects.equals(agency.getPassword(), password);
        }
    }
    @Override
    public void logout(Agency agency){
        loggedClients.remove(agency.getId());
    }

    @Override
    public synchronized Iterable<Flight> findFlights(String destination, LocalDateTime date) {
        return flightRepository.findAllDesinationDate(destination, date);
    }

    @Override
    public synchronized Iterable<Flight> findAllFlights() {
        return flightRepository.findAll();
    }

    @Override
    public void buyTickets(Long idFlight, String clientName, String address, List<String> passengers) {
        Flight flight = flightRepository.findOne(idFlight);
        Client client = clientRepository.findOneNameAddress(clientName, address);
        if (flight != null && client != null) {
            if (passengers.size() <= flight.getAvailableSeats()) {
                int remainSeats = flight.getAvailableSeats() - passengers.size();
                if (remainSeats == 0) {
                    flightRepository.delete(flight.getId());
                } else
                    flightRepository.updateSeats(flight.getId(), remainSeats);

                Booking booking = new Booking(flight, client, passengers);
                bookingRepository.save(booking);
                notifyClients();
            }
        }
    }

    private void notifyClients() {
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (Map.Entry<String, IFlightObserver> entry : loggedClients.entrySet()) {
            System.out.println(entry.getKey());
            executor.execute(() -> {
                try {
                    entry.getValue().ticketsBought();
                } catch (Exception e) {
                    System.out.println("Notify error.");
                }
            });
        }
        executor.shutdown();
    }
}

