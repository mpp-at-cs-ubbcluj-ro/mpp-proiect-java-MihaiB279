package flight.rest.agentii_turism.gui;

import flight.rest.agentii_turism.domain.Flight;
import flight.rest.agentii_turism.exceptions.Errors;
import flight.rest.agentii_turism.exceptions.InvalidFieldException;
import flight.rest.agentii_turism.service.Service;
import flight.rest.agentii_turism.validators.Validator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class MainWindow {
    ObservableList<Flight> model = FXCollections.observableArrayList();
    private Service service;
    private Validator validator;

    public void setService(Service serv) {
        service = serv;
        initializeFields();
        model.setAll((List<Flight>) service.findAllFlights());
        validator = new Validator();
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
            alert.setContentText(Errors.notSelectedFlightError);
            alert.showAndWait();
            return;
        }
        String name = nameTxt.getText();
        String address = addressTxt.getText();
        String tourists = touristsTxtArea.getText();
        List<String> touristsList = List.of(tourists.split(", "));
        try {
            validator.checkBuyData(name, touristsList);
            service.buyTickets(selectedFlight.getId(), name, address, touristsList);
            model.setAll((List<Flight>) service.findAllFlights());
        } catch (InvalidFieldException ex) {
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
            validator.checkSearchData(destination);
            Iterable<Flight> flights = service.findFlights(destination, date);
            model.setAll((List<Flight>) flights);
        } catch (InvalidFieldException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error detected");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void onClickClear() {
        model.setAll((List<Flight>) service.findAllFlights());
    }

    @FXML
    public void onClickLogOut() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/flight/rest/agentii_turism/login.fxml"));
        AnchorPane root = fxmlLoader.load();
        Login login = fxmlLoader.getController();
        login.setService(service);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        stage.show();
        closeWindow();
    }

    private void closeWindow() {
        Stage thisStage = (Stage) nameTxt.getScene().getWindow();
        thisStage.close();
    }
}