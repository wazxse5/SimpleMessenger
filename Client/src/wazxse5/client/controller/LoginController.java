package wazxse5.client.controller;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

public class LoginController {
    private ViewManager viewManager;
    private ThreadClient threadClient;
    private StringProperty serverAddress = new SimpleStringProperty();
    private IntegerProperty serverPort = new SimpleIntegerProperty();

    @FXML private TextField loginL;
    @FXML private PasswordField passwordL;
    @FXML private Label infoL;
    @FXML private Button loginButton;
    @FXML private Button loginGuestButton;

    @FXML private TextField nameR;
    @FXML private TextField surnameR;
    @FXML private TextField mailR;
    @FXML private TextField loginR;
    @FXML private PasswordField passwordR;
    @FXML private PasswordField password1R;
    @FXML private Label infoR;
    @FXML private Button registerButton;

    @FXML private TextField loginP;
    @FXML private TextField mailP;
    @FXML private Label infoP;
    @FXML private Button resetPasswordButton;

    @FXML private TextField serverAddressTF;
    @FXML private TextField serverPortTF;
    @FXML private Button connectButton;
    @FXML private Label infoC;

    public void initialize() {
        serverAddress.bind(serverAddressTF.textProperty());
        serverPortTF.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int port = Integer.parseInt(serverPortTF.getText());
                if (port < 0 || port > 65535) throw new NumberFormatException();
                infoC.setText("");
                connectButton.setDisable(false);
                serverPort.setValue(port);
            } catch (NumberFormatException e) {
                infoC.setText("Nieprawidłowy port");
                connectButton.setDisable(true);
            }
        });
    }

    public void connect() {
        String address = serverAddressTF.getText();
        int port = Integer.parseInt(serverPortTF.getText());
        threadClient.connect(address, port);
    }

    public void loginAsGuest() {
        login(true);
    }

    public void loginAsUser() {
        login(false);
    }

    private void login(boolean guest) {
        String login = loginL.getText();
        byte[] password = hash(passwordL.getText());
        threadClient.sendLoginRequest(login, password, guest);
    }

    public void register() {
        String name = nameR.getText();
        String surname = surnameR.getText();
        String mail = mailR.getText();
        String login = loginR.getText();
        if (passwordR.getText().trim().equals(password1R.getText().trim())) {
            byte[] password = hash(passwordR.getText());
            if (password != null) {
                UserInfo userInfo = new UserInfo(name, surname, mail, login, false);
                threadClient.sendRegisterRequest(userInfo, password);
            }
        } else infoR.setText("Wpisane hasła różnią się");
    }

    public void resetPassword() {

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


    public void setInfoText(String type, String text) {
        if (type.equals("L")) infoL.setText(text);
        else if (type.equals("R")) infoR.setText(text);
        else if (type.equals("C")) infoC.setText(text);
        else if (type.equals("P")) infoP.setText(text);
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    public void setThreadClient(ThreadClient threadClient) {
        this.threadClient = threadClient;
        loginButton.disableProperty().bind(threadClient.connectedProperty().not());
        loginGuestButton.disableProperty().bind(threadClient.connectedProperty().not());
        registerButton.disableProperty().bind(threadClient.connectedProperty().not());
        resetPasswordButton.disableProperty().bind(threadClient.connectedProperty().not());
    }
}
