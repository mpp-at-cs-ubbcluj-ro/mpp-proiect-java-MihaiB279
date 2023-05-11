package flight.rest.agentii_turism.gui;

import flight.rest.agentii_turism.exceptions.DataBaseException;
import flight.rest.agentii_turism.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Login {
    private Service service;
    public void setService(Service serv){
        service = serv;
    }
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;

    @FXML
    public void onClickLogInBttn() throws IOException {
        String usernamme = usernameTxt.getText();
        String password = passwordTxt.getText();
        FXMLLoader fxmlLoader = new FXMLLoader();
        try{
            service.logIn(usernamme, password);
            fxmlLoader.setLocation(getClass().getResource("/flight/rest/agentii_turism/main.fxml"));
            AnchorPane root = fxmlLoader. load();
            MainWindow mainWindow = fxmlLoader.getController();
            mainWindow.setService(service);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            stage.show();

            closeWindow();
        }
        catch (DataBaseException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error detected");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void onClickRegisterBttn() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/flight/rest/agentii_turism/register.fxml"));
        AnchorPane root = fxmlLoader. load();
        Register register = fxmlLoader.getController();
        register.setService(service);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        stage.show();

        closeWindow();
    }

    private void closeWindow() {
        Stage thisStage = (Stage) usernameTxt.getScene().getWindow();
        thisStage.close();
    }
}
