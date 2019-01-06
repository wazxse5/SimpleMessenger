package wazxse5.client.controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import wazxse5.client.ThreadClient;
import wazxse5.client.ViewManager;

public class InitController {
    private ViewManager viewManager;
    private ThreadClient threadClient;
    private StringProperty serverAddress = new SimpleStringProperty();
    private IntegerProperty serverPort = new SimpleIntegerProperty();

    @FXML private Tab loginTab;
    @FXML private Tab registerTab;
    @FXML private Tab resetPasswordTab;

    @FXML private TextField serverAddressTF;
    @FXML private TextField serverPortTF;
    @FXML private Button connectButton;
    @FXML private Button disconnectButton;
    @FXML private Label infoL;


    public void initialize() {
        serverAddress.bind(serverAddressTF.textProperty());
        serverPortTF.textProperty().addListener((observable, oldValue, newValue) -> checkServerPort(newValue));
    }

    public void connect() {
        String address = serverAddressTF.getText();
        int port = Integer.parseInt(serverPortTF.getText());
        threadClient.connect(address, port);
    }

    public void disconnect() {
        threadClient.disconnect();
    }

    private void checkServerPort(String newValue) {
        try {
            int port = Integer.parseInt(newValue);
            if (port < 0 || port > 65535) throw new NumberFormatException();
            infoL.setText("");
            connectButton.setDisable(false);
            serverPort.setValue(port);
        } catch (NumberFormatException e) {
            infoL.setText("Nieprawidłowy port");
            connectButton.setDisable(true);
        }
    }

    public Tab getLoginTab() {
        return loginTab;
    }

    public Tab getRegisterTab() {
        return registerTab;
    }

    public Tab getResetPasswordTab() {
        return resetPasswordTab;
    }

    public void setInfoText(String text) {
        infoL.setText(text);
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    public void setThreadClient(ThreadClient threadClient) {
        this.threadClient = threadClient;
        serverAddressTF.disableProperty().bind(threadClient.connectedProperty());
        serverPortTF.disableProperty().bind(threadClient.connectedProperty());
        connectButton.visibleProperty().bind(threadClient.connectedProperty().not());
        disconnectButton.visibleProperty().bind(threadClient.connectedProperty());
    }
}
