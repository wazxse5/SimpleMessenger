package wazxse5.server.controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import wazxse5.server.MysqlConnector;
import wazxse5.server.ThreadServer;
import wazxse5.server.ViewManager;

import java.io.IOException;
import java.sql.SQLException;

public class InitController {
    private ThreadServer threadServer;
    private ViewManager viewManager;
    private MysqlConnector mysqlConnector;

    private IntegerProperty serverPort = new SimpleIntegerProperty();
    @FXML private Button startServerButton;
    @FXML private TextField serverPortTF;
    @FXML private Label infoLabel;
    @FXML private TextField dbAddressTF;
    @FXML private TextField dbNameTF;
    @FXML private TextField dbUserTF;
    @FXML private TextField dbPasswordTF;

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
        try {
            connectToDatabase(dbNameTF.getText());
            threadServer.getDataLoader().setMysqlConnector(mysqlConnector);
            threadServer.start(Integer.parseInt(serverPortTF.getText()));
            viewManager.loadMainScene();
        } catch (ClassNotFoundException e) {
            infoLabel.setText("Nie można wczytać connectora bazy danych");
        } catch (SQLException e) {
            infoLabel.setText("Nie można połączyć się z bazą danych");
        }
    }

    public void createDataBase() {
        try {
            mysqlConnector = new MysqlConnector();
            mysqlConnector.create(dbAddressTF.getText(), dbNameTF.getText(), dbUserTF.getText(), dbPasswordTF.getText());
            infoLabel.setText("Baza danych została utworzona");
        } catch (ClassNotFoundException e) {
            infoLabel.setText("Nie można wczytać connectora bazy danych");
        } catch (SQLException e) {
            infoLabel.setText("Nie można połączyć się z bazą danych");
        } catch (IOException e) {
            infoLabel.setText("Nie można wczytać pliku bazy danych");
        }
    }

    private void connectToDatabase(String dbName) throws ClassNotFoundException, SQLException {
        mysqlConnector = new MysqlConnector();
        mysqlConnector.connect(dbAddressTF.getText(), dbName, dbUserTF.getText(), dbPasswordTF.getText());
    }

    public void setThreadServer(ThreadServer threadServer) {
        this.threadServer = threadServer;
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }
}
