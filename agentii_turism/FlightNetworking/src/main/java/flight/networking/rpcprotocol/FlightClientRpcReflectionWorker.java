package flight.networking.rpcprotocol;

import flight.model.Flight;
import flight.networking.dto.*;
import flight.services.IFlightObserver;
import flight.services.IFlightServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.time.LocalDateTime;


public class FlightClientRpcReflectionWorker implements Runnable, IFlightObserver {
    private IFlightServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public FlightClientRpcReflectionWorker(IFlightServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    //  private static Response errorResponse=new Response.Builder().type(ResponseType.ERROR).build();
    private Response handleRequest(Request request) {
        Response response = null;
        String handlerName = "handle" + (request).type();
        System.out.println("HandlerName " + handlerName);
        try {
            Method method = this.getClass().getDeclaredMethod(handlerName, Request.class);
            response = (Response) method.invoke(this, request);
            System.out.println("Method " + handlerName + " invoked");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return response;
    }

    private Response handleLOGIN(Request request) {
        LoginDTO logindto = (LoginDTO) request.data();
        try {
            server.logIn(logindto.getUsername(), logindto.getPassword(), this);
            return okResponse;
        } catch (Exception e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleLOGOUT(Request request) throws Exception {
        try {
            System.out.println("Logout request...");
            AgencyDTO agencyDTO = (AgencyDTO) request.data();
            server.logout(DTOUtils.getFromDTO(agencyDTO));
            connected = false;
            return okResponse;
        } catch (Exception e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleCLEAR(Request request) {
        try {
            Iterable<Flight> flights = server.findAllFlights();
            FlightDTO[] flightDTOS = DTOUtils.getDTO(flights);
            return new Response.Builder().type(ResponseType.GET_ALL_FLIGHTS).data(flightDTOS).build();
        } catch (Exception e) {
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_FOUND_FLIGHTS(Request request) {
        FlightDetailsDTO flightDetailsDTO = (FlightDetailsDTO) request.data();
        try {
            Iterable<Flight> flights = server.findFlights(flightDetailsDTO.getDestination(), flightDetailsDTO.getDepartureDateTime());
            ;
            FlightDTO[] flightDTOS = DTOUtils.getDTO(flights);
            return new Response.Builder().type(ResponseType.GET_FOUND_FLIGHTS).data(flightDTOS).build();
        } catch (Exception e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleBUY(Request request) {
        BookingDetailsDTO bookingDetailsDTO = (BookingDetailsDTO) request.data();
        try {
            server.buyTickets(bookingDetailsDTO.getIdFlight(), bookingDetailsDTO.getClientName(), bookingDetailsDTO.getAddress(), bookingDetailsDTO.getPassengers());
            return okResponse;
        } catch (Exception e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("sending response " + response);
        output.writeObject(response);
        output.flush();
    }

    @Override
    public void ticketsBought() {
        Response resp = new Response.Builder().type(ResponseType.TICKETS_BOUGHT).build();
        try {
            sendResponse(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
