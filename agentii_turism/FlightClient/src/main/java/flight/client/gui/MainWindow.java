package flight.client.gui;

import flight.model.Agency;
import flight.model.Flight;
import flight.services.IFlightObserver;
import flight.services.IFlightServices;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class MainWindow implements Initializable, IFlightObserver {
    ObservableList<Flight> model = FXCollections.observableArrayList();
    private IFlightServices service;
    private Agency user;

    public void setService(IFlightServices serv) throws Exception {
        service = serv;
        initializeFields();
        model.setAll((List<Flight>) service.findAllFlights());
    }
    public void setUser(Agency user){
        this.user = user;
    }

    @FXML
    private TextField nameTxt;
    @FXML
    private TextField destinationTxt;
    @FXML
    public TableColumn<Flight, String> destColumn;
    @FXML
    public TableColumn<Flight, String> dateColumn;
    @FXML
    public TableColumn<Flight, String> airportColumn;
    public TableColumn<Flight, String> seatsColumn;
    @FXML
    private TextField addressTxt;
    @FXML
    private TextArea touristsTxtArea;
    @FXML
    private DatePicker datePicker;
    @FXML
    public TableView<Flight> flightsTable;

    public void initializeFields() {
        destColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("departureDateTime"));
        airportColumn.setCellValueFactory(new PropertyValueFactory<>("airport"));
        seatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));
        flightsTable.setItems(model);
    }

    @FXML
    public void onClickBuy() throws IOException {
        Flight selectedFlight;
        selectedFlight = flightsTable.getSelectionModel().getSelectedItem();
        if(selectedFlight == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error detected");
            alert.setContentText("mesaj");
            alert.showAndWait();
            return;
        }
        String name = nameTxt.getText();
        String address = addressTxt.getText();
        String tourists = touristsTxtArea.getText();
        List<String> touristsList = List.of(tourists.split(", "));
        try {
            service.buyTickets(selectedFlight.getId(), name, address, touristsList);
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error detected");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void onClickSearch() throws IOException {
        String destination = destinationTxt.getText();
        LocalDateTime date = datePicker.getValue().atStartOfDay();
        try {
            Iterable<Flight> flights = service.findFlights(destination, date);
            model.setAll((List<Flight>) flights);
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error detected");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void onClickClear() throws Exception {
        model.setAll((List<Flight>) service.findAllFlights());
    }

    @FXML
    public void onClickLogOut() throws Exception {
        service.logout(user);
        closeWindow();
    }

    private void closeWindow() {
        Stage thisStage = (Stage) nameTxt.getScene().getWindow();
        thisStage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Init observer");
    }

    @Override
    public void ticketsBought() throws Exception {
        Platform.runLater(()->{
            try {
                model.setAll((List<Flight>) service.findAllFlights());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}