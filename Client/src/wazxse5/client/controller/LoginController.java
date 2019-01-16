package wazxse5.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import wazxse5.client.ThreadClient;
import wazxse5.client.ViewManager;

public class LoginController {
    private ViewManager viewManager;
    private ThreadClient threadClient;

    @FXML private TextField loginTF;
    @FXML private PasswordField passwordTF;
    @FXML private Label infoL;
    @FXML private Button loginButton;
    @FXML private Button loginGuestButton;


    public void loginAsGuest() {
        login(true);
    }

    public void loginAsUser() {
        login(false);
    }

    private void login(boolean guest) {
        String login = loginTF.getText();
        String password = passwordTF.getText();
        threadClient.sendLoginRequest(login, password, guest);
    }

    public void setInfoText(String text) {
        infoL.setText(text);
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    public void setThreadClient(ThreadClient threadClient) {
        this.threadClient = threadClient;
        loginButton.disableProperty().bind(threadClient.connectedProperty().not());
        loginGuestButton.disableProperty().bind(threadClient.connectedProperty().not());
    }
}
