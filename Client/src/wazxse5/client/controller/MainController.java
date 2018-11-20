package wazxse5.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import wazxse5.client.ThreadClient;

public class MainController {
    private Stage primaryStage;
    private ThreadClient threadClient;

    @FXML private TextField inputTF;
    @FXML private TextArea outputTA;


    public void sendMessage() {
        threadClient.send("client", inputTF.getText());
        inputTF.setText("");
    }


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setThreadClient(ThreadClient threadClient) {
        this.threadClient = threadClient;
        primaryStage.setOnCloseRequest(event -> threadClient.close());
        outputTA.textProperty().bind(threadClient.receiveProperty());
    }
}
