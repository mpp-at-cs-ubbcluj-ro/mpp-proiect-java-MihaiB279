import flight.persistence.repository.jdbc.*;
import flight.server.FlightServiceImpl;
import flight.networking.utils.AbstractServer;
import flight.networking.utils.FlightRpcConcurrentServer;
import flight.server.FlightServiceORMImpl;
import flight.services.IFlightServices;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/flightserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find flightserver.properties "+e);
            return;
        }
        //AgencyRepository agencyRepository = new AgencyRepository(serverProps);
        AgencyRepositoryORM agencyRepository = new AgencyRepositoryORM(serverProps);
        BookingRepository bookingRepository = new BookingRepository(serverProps);
        ClientRepository clientRepository = new ClientRepository(serverProps);
        FlightRepository flightRepository = new FlightRepository(serverProps);
        IFlightServices flightServerImpl=new FlightServiceORMImpl(flightRepository, clientRepository, agencyRepository, bookingRepository);
        int flightServerPort=defaultPort;
        try {
            flightServerPort = Integer.parseInt(serverProps.getProperty("flight.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+flightServerPort);
        AbstractServer server = new FlightRpcConcurrentServer(flightServerPort, flightServerImpl);
        try {
            server.start();
        } catch (Exception e) {
            System.err.println("Error starting the server" + e.getMessage());
        }finally {
            try {
                server.stop();
            }catch(Exception e){
                System.err.println("Error stopping server "+e.getMessage());
            }
        }
    }
}
