package wazxse5.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import wazxse5.client.ThreadClient;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginController {
    private Stage primaryStage;

    @FXML private TextField login;
    @FXML private PasswordField password;
    @FXML private TextField serverAddress;
    @FXML private TextField serverPort;

    public void login() {

        ThreadClient threadClient = new ThreadClient(serverAddress.getText(), Integer.parseInt(serverPort.getText()));
        try {
            threadClient.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Parent parent = loader.load();
            MainController mainController = loader.getController();
            mainController.setPrimaryStage(primaryStage);

            mainController.setThreadClient(threadClient);

            Scene scene = new Scene(parent);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
