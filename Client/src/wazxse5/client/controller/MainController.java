package wazxse5.client.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import wazxse5.client.MessageContainer;
import wazxse5.client.ThreadClient;
import wazxse5.client.ViewManager;

public class MainController {
    private ViewManager viewManager;
    private ThreadClient threadClient;

    @FXML private ListView<String> connectedClientsList;
    @FXML private TextField inputTF;
    @FXML private ScrollPane scrollPane;
    @FXML private GridPane outputGP;

    private int messageCounter = 0;

    public void initialize() {
        outputGP.prefWidthProperty().bind(scrollPane.widthProperty().subtract(10));
    }


    public void sendMessage() {
        String addressee = connectedClientsList.getSelectionModel().getSelectedItem();
        if (addressee.equals("Publiczne")) addressee = "-%&-all";
        threadClient.sendUserMessage(addressee, inputTF.getText());

        MessageContainer messageContainer = new MessageContainer(inputTF.getText());
        outputGP.addRow(messageCounter++, messageContainer);
        GridPane.setColumnIndex(messageContainer, 1);
        GridPane.setColumnSpan(messageContainer, 2);
        scrollPane.layout();
        scrollPane.setVvalue(1);
        inputTF.clear();
    }

    public void logout() {
        threadClient.logout();
    }

    public void exit() {
        threadClient.close();
        Platform.exit();
    }

    public void handleReceivedMessage(String from, String message) {
        double scrollPaneVPosition = scrollPane.getVvalue();

        MessageContainer messageContainer = new MessageContainer(from, message);
        outputGP.addRow(messageCounter++, messageContainer);
        GridPane.setColumnIndex(messageContainer, 0);
        GridPane.setColumnSpan(messageContainer, 2);

        scrollPane.layout();
        if (scrollPaneVPosition == 1.0) scrollPane.setVvalue(1);
    }

    public void setThreadClient(ThreadClient threadClient) {
        this.threadClient = threadClient;
        connectedClientsList.itemsProperty().bind(threadClient.connectedFriendsProperty());
        connectedClientsList.itemsProperty().get().add(0, "Publiczne");
        connectedClientsList.getSelectionModel().selectFirst();
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

}
