package flight.rest.services;

import flight.model.Flight;
import flight.persistence.repository.jdbc.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Properties;

@CrossOrigin
@RestController
@RequestMapping({"/agency/flights"})
public class FlightController {
    private FlightRepository flightRepository;
    private Properties serverProps;

    public FlightController() {
        Properties serverProps=new Properties();
        try {
            serverProps.load(FlightController.class.getResourceAsStream("/flightserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find flightserver.properties "+e);
        }

        flightRepository = new FlightRepository(serverProps);
    }

    @GetMapping
    public Iterable<Flight> getAll() {
        System.out.println("Get all flights ...");
        return this.flightRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        System.out.println("Get by id " + id);
        Flight flight = this.flightRepository.findOne(Long.parseLong(id));
        return flight == null ? new ResponseEntity("Flight not found", HttpStatus.NOT_FOUND) : new ResponseEntity(flight, HttpStatus.OK);
    }

    @PostMapping
    public Flight create(@RequestBody Flight flight) {
        this.flightRepository.save(flight);
        return flight;
    }

    @PutMapping("/{id}")
    public Flight update(@PathVariable String id, @RequestBody Flight flight) {
        System.out.println("Updating flight ...");
        this.flightRepository.update(Long.parseLong(id), flight);
        return flight;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        System.out.println("Deleting user ... " + id);

        try {
            this.flightRepository.delete(Long.parseLong(id));
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception var3) {
            System.out.println("Ctrl Delete user exception");
            return new ResponseEntity(var3.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
