package flight.rest.agentii_turism.gui;

import flight.rest.agentii_turism.exceptions.InvalidFieldException;
import flight.rest.agentii_turism.service.Service;
import flight.rest.agentii_turism.validators.Validator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Register {
    Service service;
    Validator validator;

    public void setService(Service serv){
        service = serv;
    }
    @FXML
    private TextField nameTxt;
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;
    @FXML
    private PasswordField repeatPasswordTxt;

    @FXML
    public void onClickSubmitBttn() throws IOException {
        String username = usernameTxt.getText();
        String name = nameTxt.getText();
        String password = passwordTxt.getText();
        String repeatPassword = repeatPasswordTxt.getText();

        FXMLLoader fxmlLoader = new FXMLLoader();
        validator = new Validator();
        try {
            validator.checkUserRegister(name, username, password, repeatPassword);

            service.addAgency(name, username, password);

            fxmlLoader.setLocation(getClass().getResource("/flight/rest/agentii_turism/login.fxml"));
            AnchorPane root = fxmlLoader.load();
            Login login = fxmlLoader.getController();
            login.setService(service);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.show();

            closeWindow();
        }
        catch (InvalidFieldException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error detected");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    private void closeWindow() {
        Stage thisStage = (Stage) usernameTxt.getScene().getWindow();
        thisStage.close();
    }
}
