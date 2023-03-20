package org.example.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.domain.Flight;
import org.example.utils.JdbcUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Properties;

public class FlightRepository implements IFlightRepository {
    private static final Logger logger = LogManager.getLogger();
    private final JdbcUtils dbUtils;

    public FlightRepository(Properties props) {
        logger.info("Initializing FlightRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Flight findOne(Long aLong) {
        logger.traceEntry();
        String sql = "SELECT * FROM flights WHERE id = ?";
        Connection con = dbUtils.getConnection();
        Flight flight = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Math.toIntExact(aLong));
            try (ResultSet resultSet = ps.executeQuery()) {

                resultSet.next();
                int id = resultSet.getInt("id");
                String destination = resultSet.getString("destination");
                LocalDateTime departureDateTime = resultSet.getTimestamp("departuredatetime").toLocalDateTime();
                String airport = resultSet.getString("airport");
                int availableSeats = resultSet.getInt("availableseats");
                flight = new Flight(destination, departureDateTime, airport, availableSeats);
                flight.setId((long) id);

                return flight;
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB" + ex);
        }

        logger.traceExit();
        return flight;
    }

    @Override
    public Iterable<Flight> findAll() {
        logger.traceEntry();
        String sql = "SELECT * FROM flights";
        Connection con = dbUtils.getConnection();
        ArrayList<Flight> flights = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet resultSet = ps.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String destination = resultSet.getString("destination");
                    LocalDateTime departureDateTime = resultSet.getTimestamp("departuredatetime").toLocalDateTime();
                    String airport = resultSet.getString("airport");
                    int availableSeats = resultSet.getInt("availableseats");
                    Flight flight = new Flight(destination, departureDateTime, airport, availableSeats);
                    flight.setId((long) id);
                    flights.add(flight);
                }
                return flights;
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB" + ex);
        }

        logger.traceExit();
        return flights;
    }

    @Override
    public Flight save(Flight entity) {
        logger.traceEntry("saving task {}", entity);
        String sql = "INSERT INTO flights (id, destination, departuredatetime, airport, availableseats) VALUES(?, ?, ?, ?, ?)";
        Connection con = dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Math.toIntExact(entity.getId()));
            ps.setString(2, entity.getDestination());
            ps.setTimestamp(3, Timestamp.valueOf(entity.getDepartureDateTime()));
            ps.setString(4, entity.getAirport());
            ps.setInt(5, entity.getAvailableSeats());

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
