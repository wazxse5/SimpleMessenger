package wazxse5.client.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import wazxse5.client.ThreadClient;
import wazxse5.client.ViewManager;

public class MainController {
    private ViewManager viewManager;
    private ThreadClient threadClient;
    private StringProperty selectedFriend = new SimpleStringProperty();

    @FXML private ListView<String> connectedClientsList;
    @FXML private TextField inputTF;
    @FXML private TextArea outputTA;

    public void initialize() {
        selectedFriend.bind(connectedClientsList.getSelectionModel().selectedItemProperty());
    }


    public void sendMessage() {
        threadClient.send(selectedFriend.get(), inputTF.getText());
        inputTF.setText("");
    }

    public void logout() {
        threadClient.logout();
    }

    public void exit() {
        threadClient.close();
        Platform.exit();
    }

    public void handleReceivedMessage(String from, String message) {
        outputTA.setText(outputTA.getText() + "\n" + from + ": " + message);
    }

    public void setThreadClient(ThreadClient threadClient) {
        this.threadClient = threadClient;
        connectedClientsList.itemsProperty().bind(threadClient.connectedFriendsProperty());
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

}
