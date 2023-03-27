package com.example.agentii_turism.service;

import com.example.agentii_turism.exceptions.Errors;
import com.example.agentii_turism.domain.Agency;
import com.example.agentii_turism.domain.Booking;
import com.example.agentii_turism.domain.Client;
import com.example.agentii_turism.domain.Flight;
import com.example.agentii_turism.exceptions.DataBaseException;
import com.example.agentii_turism.repository.AgencyRepository;
import com.example.agentii_turism.repository.BookingRepository;
import com.example.agentii_turism.repository.ClientRepository;
import com.example.agentii_turism.repository.FlightRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Service {
    private FlightRepository flightRepository;
    private ClientRepository clientRepository;
    private AgencyRepository agencyRepository;
    private BookingRepository bookingRepository;

    public Service(FlightRepository flightRepository, ClientRepository clientRepository, AgencyRepository agencyRepository, BookingRepository bookingRepository) {
        this.flightRepository = flightRepository;
        this.clientRepository = clientRepository;
        this.agencyRepository = agencyRepository;
        this.bookingRepository = bookingRepository;
    }

    public boolean logIn(String userName, String password) throws DataBaseException {
        Agency agency = agencyRepository.findOne(userName);
        if (agency == null || !Objects.equals(agency.getPassword(), password)) {
            throw new DataBaseException(Errors.nonExistingUser);
        } else return Objects.equals(agency.getPassword(), password);
    }

    public Agency findAgency(String userName){
        return agencyRepository.findOne(userName);
    }

    public Iterable<Flight> findFlights(String destination, LocalDateTime date){
        return flightRepository.findAllDesinationDate(destination, date);
    }

    public Iterable<Flight> findAllFlights(){
        return flightRepository.findAll();
    }

    public void addAgency(String name, String username, String password){
        agencyRepository.save(new Agency(username, name, password));
    }

    public void buyTickets(Long idFlight, String clientName, String address, List<String> passengers){
        Flight flight = flightRepository.findOne(idFlight);
        Client client = clientRepository.findOneNameAddress(clientName, address);
        if(flight != null && client != null){
            if(passengers.size() <= flight.getAvailableSeats()){
                int remainSeats = flight.getAvailableSeats() - passengers.size();
                if(remainSeats == 0){
                    flightRepository.delete(flight.getId());
                }
                else
                    flightRepository.updateSeats(flight.getId(), remainSeats);

                Booking booking = new Booking(flight, client, passengers);
                bookingRepository.save(booking);
            }
        }
    }
}
