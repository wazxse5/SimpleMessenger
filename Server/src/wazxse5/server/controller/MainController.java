package wazxse5.server.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import wazxse5.server.Connection;

public class MainController {
    private Stage primaryStage;

    @FXML private TableView<Connection> connectionsTable;
    @FXML private TableColumn<Connection, Integer> IDpColumn;
    @FXML private TableColumn<Connection, Integer> IDuColumn;
    @FXML private TableColumn<Connection, Integer> loginColumn;


    public void initialize() {

    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


}
