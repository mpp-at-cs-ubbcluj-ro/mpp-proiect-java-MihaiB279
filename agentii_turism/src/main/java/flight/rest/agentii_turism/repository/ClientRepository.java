package flight.rest.agentii_turism.repository;

import flight.rest.agentii_turism.domain.Client;
import flight.rest.agentii_turism.utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class ClientRepository implements IClientRepository {
    private static final Logger logger = LogManager.getLogger();
    private final JdbcUtils dbUtils;

    public ClientRepository(Properties props) {
        logger.info("Initializing ClientRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Client findOne(Long aLong) {
        logger.traceEntry();
        String sql = "SELECT * FROM clients WHERE id = ?";
        Connection con = dbUtils.getConnection();
        Client client = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Math.toIntExact(aLong));
            try (ResultSet resultSet = ps.executeQuery()) {

                resultSet.next();
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                client = new Client(name, address);
                client.setId((long) id);

                return client;
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB" + ex);
        }

        logger.traceExit();
        return client;
    }

    public Client findOneNameAddress(String nameC, String addressC) {
        logger.traceEntry();
        String sql = "SELECT * FROM clients WHERE name = ? and address = ?";
        Connection con = dbUtils.getConnection();
        Client client = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nameC);
            ps.setString(2, addressC);
            try (ResultSet resultSet = ps.executeQuery()) {

                resultSet.next();
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                client = new Client(name, address);
                client.setId((long) id);

                return client;
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB" + ex);
        }

        logger.traceExit();
        return client;
    }

    @Override
    public Iterable<Client> findAll() {
        logger.traceEntry();
        String sql = "SELECT * FROM clients";
        Connection con = dbUtils.getConnection();
        ArrayList<Client> clients = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet resultSet = ps.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String address = resultSet.getString("address");
                    Client client = new Client(name, address);
                    client.setId((long) id);
                    clients.add(client);
                }
                return clients;
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB" + ex);
        }

        logger.traceExit();
        return clients;
    }

    @Override
    public Client save(Client entity) {
        logger.traceEntry("saving task {}", entity);
        String sql = "INSERT INTO clients(id, name, address) VALUES(?, ?, ?)";
        Connection con = dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Math.toIntExact(entity.getId()));
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getAddress());

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
