package wazxse5.server.controller;

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

    @FXML private Button startServerButton;
    @FXML private TextField portTF;
    @FXML private Label infoLabel;

    public void initialize() {
        portTF.textProperty().addListener((observable, oldValue, newValue) -> checkPortTF(newValue));
    }

    public void startServer() {
        ThreadServer threadServer = new ThreadServer(Integer.parseInt(portTF.getText()));
        threadServer.start();
        primaryStage.setOnCloseRequest((observable) -> threadServer.close());

        loadMainWindow();
    }

    private void checkPortTF(String value) {
        try {
            int i = Integer.parseInt(value);
            if (i < 0 || i > 65535) throw new NumberFormatException();
            startServerButton.setDisable(false);
            infoLabel.setText("");
        } catch (NumberFormatException e) {
            startServerButton.setDisable(true);
            infoLabel.setText("Niepoprawny port");
        }
    }

    private void loadMainWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        try {
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.setTitle("Serwer działa - port: " + portTF.getText());


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
