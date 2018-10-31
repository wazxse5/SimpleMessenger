package wazxse5.server.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import wazxse5.server.ThreadServer;

import java.io.IOException;

public class InitController {
    private Stage primaryStage;


    public void startServer() {
        ThreadServer threadServer = new ThreadServer(8989);
        threadServer.start();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        try {
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);

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
