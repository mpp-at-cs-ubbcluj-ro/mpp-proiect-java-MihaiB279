package org.example;

import org.example.domain.Agency;
import org.example.domain.Booking;
import org.example.domain.Client;
import org.example.domain.Flight;
import org.example.repository.AgencyRepository;
import org.example.repository.BookingRepository;
import org.example.repository.ClientRepository;
import org.example.repository.FlightRepository;

import java.awt.print.Book;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
       testRepositories();
    }

    public static void testRepositories(){
        Properties props = new Properties();
        try {
            props.load(new FileReader("C:\\Users\\MihaiBucur\\Desktop\\Cursruri facultate\\MPP\\Lab\\Tema2\\proiect_java\\mpp-proiect-java-MihaiB279\\agentii_turism\\src\\main\\java\\org\\example\\bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }
        AgencyRepository agencyRepository = new AgencyRepository(props);
        BookingRepository bookingRepository = new BookingRepository(props);
        ClientRepository clientRepository = new ClientRepository(props);
        FlightRepository flightRepository = new FlightRepository(props);

        Agency agency1 = new Agency("ryan1", "RyanAir", "passwordRyan");
        Client client1 = new Client("RyanClient", "Strada Beta, numar 42");
        client1.setId(1L);
        Flight flight1 = new Flight("Praga", LocalDateTime.now(), "Avram Iancu", 89);
        flight1.setId(1L);
        List<String> passengers = new ArrayList<>();
        passengers.add("Ioan Costel");
        passengers.add("Matei Andrei");
        passengers.add("Elena Maria");
        passengers.add("Ana Pricop");
        Booking booking1 = new Booking(flight1, client1, passengers);
        booking1.setId(1L);

        //agencyRepository.save(agency1);
        //flightRepository.save(flight1);
        //clientRepository.save(client1);
        //bookingRepository.save(booking1);

        Agency findAgency = agencyRepository.findOne("wizz");
        System.out.println(findAgency.getId() + " " + findAgency.getName() + " " + findAgency.getPassword());
        Client findClient = clientRepository.findOne(1L);
        System.out.println(findClient.getId().toString() + " " + findClient.getName() + " " + findClient.getAddress());
        Flight findFlight = flightRepository.findOne(2L);
        System.out.println(findFlight.getId().toString() + " " + findFlight.getDestination() + " " + findFlight.getAirport() + " " + findFlight.getDepartureDateTime().toString() + " " + findFlight.getAvailableSeats());
        Booking findBooking = bookingRepository.findOne(1L);
        System.out.println(findBooking.getId().toString() + " " + findBooking.getFlight().getId().toString() + " " + findBooking.getClient().getId().toString());

        Iterable<Agency> listAgencies = agencyRepository.findAll();
        for (Agency agency : listAgencies) {
            System.out.println(agency.getId() + " " + agency.getName() + " " + agency.getPassword());
        }
        Iterable<Client> listClients = clientRepository.findAll();
        for (Client client : listClients) {
            System.out.println(client.getId().toString() + " " + client.getName() + " " + client.getAddress());
        }
        Iterable<Flight> listFlights = flightRepository.findAll();
        for (Flight flight : listFlights) {
            System.out.println(flight.getId().toString() + " " + flight.getDestination() + " " + flight.getAirport() + " " + flight.getDepartureDateTime().toString() + " " + flight.getAvailableSeats());
        }
        Iterable<Booking> listBookings = bookingRepository.findAll();
        for (Booking booking : listBookings) {
            System.out.println(booking.getId().toString() + " " + booking.getFlight().getId().toString() + " " + booking.getClient().getId().toString());
        }
    }
}