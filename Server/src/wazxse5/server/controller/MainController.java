package wazxse5.server.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import wazxse5.server.Connection;
import wazxse5.server.ThreadServer;
import wazxse5.server.ViewManager;

public class MainController {
    private ThreadServer threadServer;
    private ViewManager viewManager;

    @FXML private TableView<Connection> connectionsTable;
    @FXML private TableColumn<Connection, Integer> IDpColumn;
    @FXML private TableColumn<Connection, Integer> IDuColumn;
    @FXML private TableColumn<Connection, String> loginColumn;


    public void initialize() {

    }

    public void setThreadServer(ThreadServer threadServer) {
        this.threadServer = threadServer;
        connectionsTable.setItems(threadServer.getConnectedConnections());
        // FIXME: 28.12.2018 
//        loginColumn.setCellValueFactory(param -> param.getValue().userProperty().get().getUserInfo().getLogin());
//        IDuColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper(param.getValue().getUser().getUserInfo().getId()));
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }
}
