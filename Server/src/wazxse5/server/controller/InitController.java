package wazxse5.server.controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import wazxse5.server.ThreadServer;
import wazxse5.server.ViewManager;

public class InitController {
    private ThreadServer threadServer;
    private ViewManager viewManager;

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
        threadServer.start(Integer.parseInt(serverPortTF.getText()));
        viewManager.loadMainScene();
    }

    public void setThreadServer(ThreadServer threadServer) {
        this.threadServer = threadServer;
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }
}
