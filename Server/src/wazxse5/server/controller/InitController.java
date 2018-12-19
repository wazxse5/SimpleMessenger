package wazxse5.server.controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import wazxse5.server.ThreadServer;

import java.io.IOException;

public class InitController {
    private Stage primaryStage;
    private IntegerProperty serverPort = new SimpleIntegerProperty();

    @FXML private Button startServerButton;
    @FXML private TextField serverPortTF;
    @FXML private Label infoLabel;

    public void initialize() {
        serverPortTF.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int port = Integer.parseInt(serverPortTF.getText());
                if (port < 0 || port > 65535) throw new NumberFormatException();
                infoLabel.setText("");
                startServerButton.setDisable(false);
                serverPort.setValue(port);
            } catch (NumberFormatException e) {
                infoLabel.setText("Nieprawidłowy port");
                startServerButton.setDisable(true);
            }
        });
    }

    public void startServer() {
        ThreadServer threadServer = new ThreadServer(Integer.parseInt(serverPortTF.getText()));
        threadServer.start();
        primaryStage.setOnCloseRequest((observable) -> threadServer.close());

        loadMainWindow();
    }

    private void loadMainWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        try {
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.setTitle("Serwer działa - port: " + serverPortTF.getText());


            MainController mainController = loader.getController();
            mainController.setPrimaryStage(primaryStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
