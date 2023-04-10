package flight.networking.rpcprotocol;

import flight.model.Agency;
import flight.model.Flight;
import flight.networking.dto.*;
import flight.services.IFlightObserver;
import flight.services.IFlightServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class FlightServicesRpcProxy implements IFlightServices {
    private String host;
    private int port;

    private IFlightObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public FlightServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<Response>();
    }

    private void sendRequest(Request request)throws Exception {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new Exception("Error sending object "+e);
        }

    }

    private Response readResponse() throws Exception {
        Response response=null;
        try{

            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection() throws Exception {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
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


    private void handleUpdate(Response response) throws Exception {
        try {
            client.ticketsBought();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isUpdate(Response response){
        return response.type()== ResponseType.TICKETS_BOUGHT;
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
        LoginDTO ldto= new LoginDTO(userName, password);
        Request req=new Request.Builder().type(RequestType.LOGIN).data(ldto).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.OK){
            this.client=clientObs;
            return true;
        }
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new Exception(err);
        }
        return false;
    }
    @Override
    public void logout(Agency agency) throws Exception {
        AgencyDTO agencyDTO = new AgencyDTO(agency.getId(), agency.getName(), agency.getPassword());
        Request req=new Request.Builder().type(RequestType.LOGOUT).data(agencyDTO).build();
        sendRequest(req);
        Response response=readResponse();
        closeConnection();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new Exception(err);
        }
    }

    @Override
    public Iterable<Flight> findFlights(String destination, LocalDateTime date) throws Exception {
        FlightDetailsDTO fddto= new FlightDetailsDTO(destination, date);
        Request req=new Request.Builder().type(RequestType.GET_FOUND_FLIGHTS).data(fddto).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new Exception(err);
        }
        FlightDTO[] flightsDTO=(FlightDTO[])response.data();
        return DTOUtils.getFromDTO(flightsDTO);
    }

    @Override
    public Iterable<Flight> findAllFlights() throws Exception {
        Request req=new Request.Builder().type(RequestType.CLEAR).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new Exception(err);
        }
        FlightDTO[] flightsDTO=(FlightDTO[])response.data();
        return DTOUtils.getFromDTO(flightsDTO);
    }

    @Override
    public void buyTickets(Long idFlight, String clientName, String address, List<String> passengers) throws Exception {
        BookingDetailsDTO fddto= new BookingDetailsDTO(idFlight, clientName, passengers, address);
        Request req=new Request.Builder().type(RequestType.BUY).data(fddto).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new Exception(err);
        }
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }else{

                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
