package wazxse5.server.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import wazxse5.server.Connection;
import wazxse5.server.ThreadServer;
import wazxse5.server.ViewManager;

public class MainController {
    private ThreadServer threadServer;
    private ViewManager viewManager;

    @FXML private TableView<Connection> connectionsTable;
    @FXML private TableColumn<Connection, String> IDpColumn;
    @FXML private TableColumn<Connection, String> IDuColumn;
    @FXML private TableColumn<Connection, String> loginColumn;
    @FXML private TableColumn<Connection, String> nameColumn;
    @FXML private TableColumn<Connection, String> surnameColumn;
    @FXML private TableColumn<Connection, String> mailColumn;


    public void initialize() {

    }

    public void setThreadServer(ThreadServer threadServer) {
        this.threadServer = threadServer;

        connectionsTable.setItems(threadServer.getConnectedConnections());
        IDpColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        IDuColumn.setCellValueFactory(param -> param.getValue().getUser().idProperty());
        loginColumn.setCellValueFactory(param -> param.getValue().getUser().loginProperty());
        nameColumn.setCellValueFactory(param -> param.getValue().getUser().nameProperty());
        surnameColumn.setCellValueFactory(param -> param.getValue().getUser().surnameProperty());
        mailColumn.setCellValueFactory(param -> param.getValue().getUser().mailProperty());
    }

    public void refreshConnectionsTable() {
        Platform.runLater(() -> connectionsTable.refresh());
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }
}
