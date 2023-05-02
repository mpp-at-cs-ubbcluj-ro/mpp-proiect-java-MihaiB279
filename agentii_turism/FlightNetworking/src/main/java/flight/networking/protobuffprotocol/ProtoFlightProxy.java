package flight.networking.protobuffprotocol;

import flight.model.Agency;
import flight.model.Flight;
import flight.services.IFlightObserver;
import flight.services.IFlightServices;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProtoFlightProxy  implements IFlightServices {
    private String host;
    private int port;

    private IFlightObserver client;

    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private BlockingQueue<FlightProtobufs.FlightResponse> qresponses;
    private volatile boolean finished;
    public ProtoFlightProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<FlightProtobufs.FlightResponse>();
    }

    private void sendRequest(FlightProtobufs.FlightRequest request)throws Exception {
        try {
            System.out.println("Sending request ..."+request);
            request.writeDelimitedTo(output);
            output.flush();
            System.out.println("Request sent.");
        } catch (IOException e) {
            throw new Exception("Error sending object "+e);
        }

    }

    private FlightProtobufs.FlightResponse readResponse() throws Exception {
        FlightProtobufs.FlightResponse response=null;
        try{
            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection(){
        try {
            connection=new Socket(host,port);
            output=connection.getOutputStream();
            input=connection.getInputStream();     //new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(FlightProtobufs.FlightResponse response){
        try {
            List<Flight> flights=ProtoUtils.getFlights(response);
            client.ticketsBought(flights);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isUpdate(FlightProtobufs.FlightResponse.Type type){
        return type == FlightProtobufs.FlightResponse.Type.TicketsBought;
    }
    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public boolean logIn(String userName, String password, IFlightObserver clientObs) throws Exception {
        initializeConnection();
        System.out.println("Login request ...");
        sendRequest(ProtoUtils.createLoginRequest(userName, password));
        FlightProtobufs.FlightResponse response=readResponse();
        if (response.getType()==FlightProtobufs.FlightResponse.Type.Ok){
            this.client=clientObs;
            return true;
        }
        if (response.getType()==FlightProtobufs.FlightResponse.Type.Error){
            String errorText=ProtoUtils.getError(response);
            closeConnection();
            throw new Exception(errorText);
        }

        return false;
    }
    @Override
    public void logout(Agency agency) throws Exception {
        sendRequest(ProtoUtils.createLogoutRequest(agency));
        FlightProtobufs.FlightResponse response=readResponse();
        closeConnection();
        if (response.getType()==FlightProtobufs.FlightResponse.Type.Error){
            String errorText=ProtoUtils.getError(response);
            throw new Exception(errorText);
        }
    }

    @Override
    public Iterable<Flight> findFlights(String destination, LocalDateTime date) throws Exception {
        sendRequest(ProtoUtils.createGetFoundFlightsRequest(destination, date));
        FlightProtobufs.FlightResponse response=readResponse();
        if (response.getType()== FlightProtobufs.FlightResponse.Type.Error){
            String errorText=ProtoUtils.getError(response);
            throw new Exception(errorText);
        }
        List<Flight> flights=ProtoUtils.getFlights(response);
        return flights;
    }

    @Override
    public Iterable<Flight> findAllFlights() throws Exception {
        sendRequest(ProtoUtils.createClearRequest());
        FlightProtobufs.FlightResponse response=readResponse();
        if (response.getType()== FlightProtobufs.FlightResponse.Type.Error){
            String errorText=ProtoUtils.getError(response);
            throw new Exception(errorText);
        }
        List<Flight> flights=ProtoUtils.getFlights(response);
        return flights;
    }

    @Override
    public void buyTickets(Long idFlight, String clientName, String address, List<String> passengers) throws Exception {
        sendRequest(ProtoUtils.createBuyRequest(idFlight, clientName, address, passengers));
        FlightProtobufs.FlightResponse response=readResponse();
        if (response.getType()== FlightProtobufs.FlightResponse.Type.Error){
            String errorText=ProtoUtils.getError(response);
            throw new Exception(errorText);
        }
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    FlightProtobufs.FlightResponse response=FlightProtobufs.FlightResponse.parseDelimitedFrom(input);
                    System.out.println("response received "+response);

                    if (isUpdate(response.getType())){
                        handleUpdate(response);
                    }else{
                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
