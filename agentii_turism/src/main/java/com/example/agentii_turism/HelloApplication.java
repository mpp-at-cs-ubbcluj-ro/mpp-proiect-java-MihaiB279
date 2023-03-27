package com.example.agentii_turism;

import com.example.agentii_turism.gui.Login;
import com.example.agentii_turism.repository.AgencyRepository;
import com.example.agentii_turism.repository.BookingRepository;
import com.example.agentii_turism.repository.ClientRepository;
import com.example.agentii_turism.repository.FlightRepository;
import com.example.agentii_turism.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Properties props = new Properties();
        try {
            props.load(new FileReader("C:\\Users\\MihaiBucur\\Desktop\\Cursruri facultate\\MPP\\Lab\\Tema2\\proiect_java\\mpp-proiect-java-MihaiB279\\agentii_turism\\src\\main\\java\\com\\example\\agentii_turism\\bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }
        AgencyRepository agencyRepository = new AgencyRepository(props);
        BookingRepository bookingRepository = new BookingRepository(props);
        ClientRepository clientRepository = new ClientRepository(props);
        FlightRepository flightRepository = new FlightRepository(props);
        Service service = new Service(flightRepository, clientRepository, agencyRepository, bookingRepository);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        Login login = fxmlLoader.getController();
        login.setService(service);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}