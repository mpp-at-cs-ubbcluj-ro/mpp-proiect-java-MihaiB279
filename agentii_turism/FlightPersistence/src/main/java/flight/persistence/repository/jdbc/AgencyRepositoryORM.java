package flight.persistence.repository.jdbc;

import flight.model.Agency;
import flight.persistence.IAgencyRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

public class AgencyRepositoryORM implements IAgencyRepository {
    private static final Logger logger = LogManager.getLogger();
    private final JdbcUtils dbUtils;

    static SessionFactory sessionFactory;

    public AgencyRepositoryORM(Properties props) {
        logger.info("Initializing AgencyRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
        initialize();
    }

    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exceptie "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    @Override
    public Agency findOne(String s) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Query<Agency> query = session.createQuery("FROM Agency AS a WHERE a.id = :username", Agency.class);
                query.setParameter("username", s);
                List<Agency> agencyList = query.getResultList();
                if (!agencyList.isEmpty()) {
                    Agency agency = agencyList.get(0);
                    tx.commit();
                    return agency;
                } else {
                    System.err.println("Agency not found with name: " + s);
                    return null;
                }
            } catch (RuntimeException ex) {
                System.err.println("Error while selecting Agency with name: " + s);
                if (tx != null) tx.rollback();
                return null;
            }
        }
    }

    @Override
    public Iterable<Agency> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List<Agency> agencies = session.createQuery("from Agency as a", Agency.class).list();
                System.out.println(agencies.size() + " message(s) found:");
                for (Agency a : agencies) {
                    System.out.println(a.getName() + ' ' + a.getId());
                }
                tx.commit();

                return agencies;
            } catch (RuntimeException ex) {
                System.err.println("Eroare la select " + ex);
                if (tx != null)
                    tx.rollback();
                return null;
            }
        }
    }


    @Override
    public Agency save(Agency entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(entity);
                tx.commit();
                return null;
            } catch (RuntimeException ex) {
                System.err.println("Eroare la inserare " + ex);
                if (tx != null)
                    tx.rollback();
                return entity;
            }
        }
    }

}
