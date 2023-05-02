package flight.networking.protobuffprotocol;

import com.google.protobuf.Timestamp;
import flight.model.Agency;
import flight.model.Flight;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class ProtoUtils {
    public static FlightProtobufs.FlightRequest createLoginRequest(String username, String password) {
        FlightProtobufs.Login loginDTO = FlightProtobufs.Login.newBuilder().setUsername(username).setPassword(password).build();
        FlightProtobufs.FlightRequest request = FlightProtobufs.FlightRequest.newBuilder().setType(FlightProtobufs.FlightRequest.Type.Login).setLogin(loginDTO).build();
        return request;
    }

    public static FlightProtobufs.FlightRequest createLogoutRequest(Agency agency) {
        FlightProtobufs.Agency agencyDTO = FlightProtobufs.Agency.newBuilder().setId(agency.getId()).setName(agency.getName()).setPassword(agency.getPassword()).build();
        FlightProtobufs.FlightRequest request = FlightProtobufs.FlightRequest.newBuilder().setType(FlightProtobufs.FlightRequest.Type.Logout).setAgency(agencyDTO).build();
        return request;
    }

    public static FlightProtobufs.FlightRequest createBuyRequest(Long idFlight, String clientName, String address, List<String> passengers) {
        FlightProtobufs.BookingDetails bookingDetailsDTO = FlightProtobufs.BookingDetails.newBuilder().setIdFlight(idFlight).setAddress(address).setClientName(clientName).addAllPassengers(passengers).build();
        FlightProtobufs.FlightRequest request = FlightProtobufs.FlightRequest.newBuilder().setType(FlightProtobufs.FlightRequest.Type.Buy).setBookingDetails(bookingDetailsDTO).build();
        return request;
    }

    public static FlightProtobufs.FlightRequest createGetFoundFlightsRequest(String destination, LocalDateTime date) {
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(date.toEpochSecond(ZoneOffset.UTC))
                .setNanos(date.getNano())
                .build();
        FlightProtobufs.FlightDetails flightDetailsDTO = FlightProtobufs.FlightDetails.newBuilder().setDestination(destination).setDepartureDateTime(timestamp).build();
        FlightProtobufs.FlightRequest request = FlightProtobufs.FlightRequest.newBuilder().setType(FlightProtobufs.FlightRequest.Type.GetFoundFlights).setFlightDetails(flightDetailsDTO).build();
        return request;
    }

    public static FlightProtobufs.FlightRequest createClearRequest() {
        FlightProtobufs.FlightRequest request = FlightProtobufs.FlightRequest.newBuilder().setType(FlightProtobufs.FlightRequest.Type.Clear).build();
        return request;
    }

    public static FlightProtobufs.FlightResponse createOkResponse(){
        FlightProtobufs.FlightResponse response = FlightProtobufs.FlightResponse.newBuilder().setType(FlightProtobufs.FlightResponse.Type.Ok).build();
        return response;
    }

    public static FlightProtobufs.FlightResponse createErrorResponse(String error){
        FlightProtobufs.FlightResponse response = FlightProtobufs.FlightResponse.newBuilder().setType(FlightProtobufs.FlightResponse.Type.Error).setError(error).build();
        return response;
    }

    public static FlightProtobufs.FlightResponse createGetAllFlightsResponse(Iterable<Flight> flights){
        List<FlightProtobufs.Flight> flightsDTO = new ArrayList<>();
        for(Flight flight: flights){
            LocalDateTime date = flight.getDepartureDateTime();
            Timestamp timestamp = Timestamp.newBuilder()
                    .setSeconds(date.toEpochSecond(ZoneOffset.UTC))
                    .setNanos(date.getNano())
                    .build();
            FlightProtobufs.Flight flightDTO = FlightProtobufs.Flight.newBuilder().setId(flight.getId())
                    .setAirport(flight.getAirport())
                    .setAvailableSeats(flight.getAvailableSeats())
                    .setDestination(flight.getDestination())
                    .setDepartureDateTime(timestamp)
                    .build();

            flightsDTO.add(flightDTO);
        }
        FlightProtobufs.FlightResponse response = FlightProtobufs.FlightResponse.newBuilder().setType(FlightProtobufs.FlightResponse.Type.GetAllFLights).addAllFlights(flightsDTO).build();
        return response;
    }
    public static FlightProtobufs.FlightResponse createUpdateBoughtResponse(Iterable<Flight> flights){
        List<FlightProtobufs.Flight> flightsDTO = new ArrayList<>();
        for(Flight flight: flights){
            LocalDateTime date = flight.getDepartureDateTime();
            Timestamp timestamp = Timestamp.newBuilder()
                    .setSeconds(date.toEpochSecond(ZoneOffset.UTC))
                    .setNanos(date.getNano())
                    .build();
            FlightProtobufs.Flight flightDTO = FlightProtobufs.Flight.newBuilder().setId(flight.getId())
                    .setAirport(flight.getAirport())
                    .setAvailableSeats(flight.getAvailableSeats())
                    .setDestination(flight.getDestination())
                    .setDepartureDateTime(timestamp)
                    .build();

            flightsDTO.add(flightDTO);
        }
        FlightProtobufs.FlightResponse response = FlightProtobufs.FlightResponse.newBuilder().setType(FlightProtobufs.FlightResponse.Type.TicketsBought).addAllFlights(flightsDTO).build();
        return response;
    }
    public static FlightProtobufs.FlightResponse createGetFoundFlightsResponse(Iterable<Flight> flights){
        List<FlightProtobufs.Flight> flightsDTO = new ArrayList<>();
        for(Flight flight: flights){
            LocalDateTime date = flight.getDepartureDateTime();
            Timestamp timestamp = Timestamp.newBuilder()
                    .setSeconds(date.toEpochSecond(ZoneOffset.UTC))
                    .setNanos(date.getNano())
                    .build();
            FlightProtobufs.Flight flightDTO = FlightProtobufs.Flight.newBuilder().setId(flight.getId())
                    .setAirport(flight.getAirport())
                    .setAvailableSeats(flight.getAvailableSeats())
                    .setDestination(flight.getDestination())
                    .setDepartureDateTime(timestamp)
                    .build();

            flightsDTO.add(flightDTO);
        }
        FlightProtobufs.FlightResponse response = FlightProtobufs.FlightResponse.newBuilder().setType(FlightProtobufs.FlightResponse.Type.GetFoundFlights).addAllFlights(flightsDTO).build();
        return response;
    }

    public static String getError(FlightProtobufs.FlightResponse response){
        String errorMessage=response.getError();
        return errorMessage;
    }

    public static List<Flight> getFlights(FlightProtobufs.FlightResponse response){
        int size = response.getFlightsCount();
        List<Flight> flights = new ArrayList<>();
        for(int i = 0; i < size; i ++){
            FlightProtobufs.Flight flightDTO = response.getFlights(i);
            Timestamp timestamp = flightDTO.getDepartureDateTime();
            LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(
                    timestamp.getSeconds(),
                    timestamp.getNanos(),
                    ZoneOffset.UTC
            );
            Flight flight = new Flight(flightDTO.getDestination(), localDateTime,
                    flightDTO.getAirport(), flightDTO.getAvailableSeats());
            flight.setId(flightDTO.getId());
            flights.add(flight);
        }

        return flights;
    }

    public static Agency getAgency(FlightProtobufs.FlightRequest request){
        FlightProtobufs.Agency agency = request.getAgency();
        return new Agency(agency.getId(), agency.getName(), agency.getPassword());
    }

}
