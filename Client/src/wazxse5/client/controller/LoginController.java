package wazxse5.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import wazxse5.client.ThreadClient;
import wazxse5.client.ViewManager;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        byte[] password = hash(passwordTF.getText());
        threadClient.sendLoginRequest(login, password, guest);
    }

    private byte[] hash(String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = text.trim().getBytes(StandardCharsets.UTF_8);
            return messageDigest.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
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
