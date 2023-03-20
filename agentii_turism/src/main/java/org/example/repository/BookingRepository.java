package org.example.repository;

import org.example.domain.Agency;
import org.example.domain.Booking;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.domain.Client;
import org.example.domain.Flight;
import org.example.utils.JdbcUtils;

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
            ps.setInt(1, Math.toIntExact(entity.getId()));
            ps.setInt(2, Math.toIntExact(entity.getFlight().getId()));
            ps.setInt(3, Math.toIntExact(entity.getClient().getId()));
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
