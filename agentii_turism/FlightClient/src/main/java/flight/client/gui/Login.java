package flight.client.gui;

import flight.model.Agency;
import flight.services.IFlightServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Login {
    private IFlightServices service;
    private MainWindow mainCtrl;
    Parent mainChatParent;
    public void setService(IFlightServices serv){
        service = serv;
    }
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;

    @FXML
    public void onClickLogInBttn(){
        String usernamme = usernameTxt.getText();
        String password = passwordTxt.getText();
        try{
            service.logIn(usernamme, password, mainCtrl);
            mainCtrl.setService(service);
            mainCtrl.setUser(new Agency(usernamme, usernamme, password));
            Stage stage=new Stage();
            stage.setScene(new Scene(mainChatParent));
            stage.show();

            closeWindow();
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
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

    public void setMainController(MainWindow mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void setParent(Parent p){
        mainChatParent=p;
    }
}
