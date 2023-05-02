package flight.client;

import flight.client.gui.Login;
import flight.client.gui.MainWindow;
import flight.networking.protobuffprotocol.ProtoFlightProxy;
import flight.services.IFlightServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class StartProtobuffClientFX  extends Application {
    private static int defaultFlightPort = 55555;
    private static String defaultServer = "localhost";

    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartProtobuffClientFX.class.getResourceAsStream("/flightclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find flightclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("flight.server.host", defaultServer);
        int serverPort = defaultFlightPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("flight.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultFlightPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IFlightServices server = new ProtoFlightProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
        Parent root=loader.load();

        Login loginCtrl = loader.<Login>getController();
        loginCtrl.setService(server);

        FXMLLoader mainLoader = new FXMLLoader(getClass().getClassLoader().getResource("main.fxml"));
        Parent croot= mainLoader.load();

        MainWindow mainCtrl = mainLoader.<MainWindow>getController();

        loginCtrl.setMainController(mainCtrl);
        loginCtrl.setParent(croot);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

}
