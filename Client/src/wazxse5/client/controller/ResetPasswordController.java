package wazxse5.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import wazxse5.client.ThreadClient;
import wazxse5.client.ViewManager;

public class ResetPasswordController {
    private ViewManager viewManager;
    private ThreadClient threadClient;

    @FXML private TextField loginTF;
    @FXML private TextField mailTF;
    @FXML private Label infoL;
    @FXML private Button resetPasswordButton;

    public void resetPassword() {

    }

    public void setInfoText(String text) {
        infoL.setText(text);
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    public void setThreadClient(ThreadClient threadClient) {
        this.threadClient = threadClient;
        resetPasswordButton.disableProperty().bind(threadClient.connectedProperty().not());
    }
}
