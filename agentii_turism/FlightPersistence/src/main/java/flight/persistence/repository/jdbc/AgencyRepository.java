package flight.persistence.repository.jdbc;

import flight.model.Agency;
import flight.persistence.IAgencyRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class AgencyRepository implements IAgencyRepository {
    private static final Logger logger = LogManager.getLogger();
    private final JdbcUtils dbUtils;

    public AgencyRepository(Properties props) {
        logger.info("Initializing AgencyRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Agency findOne(String s) {
        logger.traceEntry();
        String sql = "SELECT * FROM agencies WHERE username = ?";
        Connection con = dbUtils.getConnection();
        Agency agency = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s);
            try (ResultSet resultSet = ps.executeQuery()) {

                resultSet.next();
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                String username = resultSet.getString("username");
                agency = new Agency(username, name, password);

                return agency;
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB" + ex);
        }

        logger.traceExit();
        return agency;
    }

    @Override
    public Iterable<Agency> findAll() {
        logger.traceEntry();
        String sql = "SELECT * FROM agencies";
        Connection con = dbUtils.getConnection();
        ArrayList<Agency> agencies = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            try (ResultSet resultSet = ps.executeQuery()) {

                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String password = resultSet.getString("password");
                    String username = resultSet.getString("username");
                    Agency agency = new Agency(username, name, password);
                    agencies.add(agency);
                }
                return agencies;
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB" + ex);
        }

        logger.traceExit();
        return agencies;
    }


    @Override
    public Agency save(Agency entity) {
        logger.traceEntry("saving task {}", entity);
        String sql = "INSERT INTO agencies(username, name, password) VALUES(?, ?, ?)";
        Connection con = dbUtils.getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, entity.getId());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getPassword());

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
