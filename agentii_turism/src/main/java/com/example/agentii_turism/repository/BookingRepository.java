package com.example.agentii_turism.repository;

import com.example.agentii_turism.domain.Booking;
import com.example.agentii_turism.domain.Client;
import com.example.agentii_turism.domain.Flight;
import com.example.agentii_turism.utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BookingRepository implements IBookingRepository {
    private static final Logger logger = LogManager.getLogger();

    private final JdbcUtils dbUtils;

    public BookingRepository(Properties props) {
        logger.info("Initializing BookingRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Booking findOne(Long aLong) {
        logger.traceEntry();
        String sql = "SELECT * FROM bookings inner join clients on id_client=clients.id" +
                " inner join flights on id_flight=flights.id where bookings.id = ?";
        Connection con = dbUtils.getConnection();
        Booking booking = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, aLong);
            try (ResultSet resultSet = ps.executeQuery()) {

                resultSet.next();
                int id_b = resultSet.getInt("id");
                int id_flight = resultSet.getInt("id_flight");
                int id_client = resultSet.getInt("id_client");
                String passengers = resultSet.getString("passengers");
                List<String> passengersList = List.of(passengers.split(","));
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String destination = resultSet.getString("destination");
                LocalDateTime departureDateTime = resultSet.getTimestamp("departuredatetime").toLocalDateTime();
                String airport = resultSet.getString("airport");
                int availableSeats = resultSet.getInt("availableseats");

                Flight flight = new Flight(destination, departureDateTime, airport, availableSeats);
                flight.setId((long) id_flight);
                Client client = new Client(name, address);
                client.setId((long) id_client);
                booking = new Booking(flight, client, passengersList);
                booking.setId((long) id_b);

                return booking;
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB" + ex);
        }

        logger.traceExit();
        return booking;
    }

    @Override
    public Iterable<Booking> findAll() {
        logger.traceEntry();
        String sql = "SELECT * FROM bookings inner join clients on id_client=clients.id" +
                " inner join flights on id_flight=flights.id";
        Connection con = dbUtils.getConnection();
        ArrayList<Booking> bookings = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet resultSet = ps.executeQuery()) {

                while (resultSet.next()) {
                    int id_b = resultSet.getInt("id");
                    int id_flight = resultSet.getInt("id_flight");
                    int id_client = resultSet.getInt("id_client");
                    String passengers = resultSet.getString("passengers");
                    List<String> passengersList = List.of(passengers.split(","));
                    String name = resultSet.getString("name");
                    String address = resultSet.getString("address");
                    String destination = resultSet.getString("destination");
                    LocalDateTime departureDateTime = resultSet.getTimestamp("departuredatetime").toLocalDateTime();
                    String airport = resultSet.getString("airport");
                    int availableSeats = resultSet.getInt("availableseats");

                    Flight flight = new Flight(destination, departureDateTime, airport, availableSeats);
                    flight.setId((long) id_flight);
                    Client client = new Client(name, address);
                    client.setId((long) id_client);
                    Booking booking = new Booking(flight, client, passengersList);
                    booking.setId((long) id_b);

                    bookings.add(booking);
                }
                return bookings;
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB" + ex);
        }

        logger.traceExit();
        return bookings;
    }

    private Long getNewId() {
        String sql = "SELECT MAX(id) FROM bookings";
        Long id = null;
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getLong(1);
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB" + ex);
        }
        return id;
    }

    @Override
    public Booking save(Booking entity) {
        logger.traceEntry("saving task {}", entity);
        String sql = "INSERT INTO bookings(id, id_flight, id_client, passengers) VALUES(?, ?, ?, ?)";
        Connection con = dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            StringBuilder passengers = new StringBuilder();
            List<String> passengersList = entity.getPassengers();
            for (String passenger : passengersList) {
                if (passengers.isEmpty())
                    passengers.append(passenger);
                else passengers.append(",").append(passenger);
            }
            ps.setLong(1, getNewId() + 1);
            ps.setLong(2, entity.getFlight().getId());
            ps.setLong(3, entity.getClient().getId());
            ps.setString(4, String.valueOf(passengers));

            int result = ps.executeUpdate();
            logger.trace("Saved {} instances", result);

            return null;
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB" + ex);
        }
        return entity;
    }
}
