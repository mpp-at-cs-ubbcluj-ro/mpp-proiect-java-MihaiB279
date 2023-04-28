package flight.networking.utils;

import flight.networking.rpcprotocol.FlightClientRpcReflectionWorker;
import flight.services.IFlightServices;

import java.net.Socket;


public class FlightRpcConcurrentServer extends AbsConcurrentServer {
    private IFlightServices flightServer;
    public FlightRpcConcurrentServer(int port, IFlightServices flightServer) {
        super(port);
        this.flightServer = flightServer;
        System.out.println("Flight- FlightRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        // ChatClientRpcWorker worker=new ChatClientRpcWorker(chatServer, client);
        FlightClientRpcReflectionWorker worker=new FlightClientRpcReflectionWorker(flightServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
