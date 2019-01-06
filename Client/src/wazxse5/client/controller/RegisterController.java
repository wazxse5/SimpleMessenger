package wazxse5.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import wazxse5.client.ThreadClient;
import wazxse5.client.ViewManager;
import wazxse5.common.UserInfo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterController {
    private ViewManager viewManager;
    private ThreadClient threadClient;

    @FXML private TextField nameTF;
    @FXML private TextField surnameTF;
    @FXML private TextField mailTF;
    @FXML private TextField loginTF;
    @FXML private PasswordField passwordPF;
    @FXML private PasswordField passwordPF1;
    @FXML private Label infoL;
    @FXML private Button registerButton;

    public void register() {
        String name = nameTF.getText();
        String surname = surnameTF.getText();
        String mail = mailTF.getText();
        String login = loginTF.getText();
        if (passwordPF.getText().trim().equals(passwordPF1.getText().trim())) {
            byte[] password = hash(passwordPF.getText());
            if (password != null) {
                UserInfo userInfo = new UserInfo(-1, name, surname, mail, login, null, -1, false);
                threadClient.sendRegisterRequest(userInfo, password);
            }
        } else infoL.setText("Wpisane hasła różnią się");
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
        registerButton.disableProperty().bind(threadClient.connectedProperty().not());
    }
}
