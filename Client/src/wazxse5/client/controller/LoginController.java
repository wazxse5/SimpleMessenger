package wazxse5.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import wazxse5.client.ThreadClient;
import wazxse5.client.ViewManager;
import wazxse5.common.UserInfo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginController {
    private ViewManager viewManager;
    private ThreadClient threadClient;
    private MessageDigest messageDigest;

    @FXML private TextField loginL;
    @FXML private PasswordField passwordL;
    @FXML private Label infoL;

    @FXML private TextField nameR;
    @FXML private TextField surnameR;
    @FXML private TextField mailR;
    @FXML private TextField loginR;
    @FXML private PasswordField passwordR;
    @FXML private PasswordField password1R;
    @FXML private Label infoR;

    @FXML private TextField loginP;
    @FXML private TextField mailP;

    @FXML private TextField serverAddressTF;
    @FXML private TextField serverPortTF;

    public void initialize() {

        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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

    public void register() {
        try {
            String address = serverAddressTF.getText();
            int port = Integer.parseInt(serverPortTF.getText());
            if (port < 0 || port > 65535) throw new NumberFormatException();

            String name = nameR.getText();
            String surname = surnameR.getText();
            String mail = mailR.getText();
            String login = loginR.getText();
            byte[] password = hash(passwordR.getText());
            UserInfo userInfo = new UserInfo(name, surname, mail, login, false);

            threadClient.sendRegisterRequest(address, port, userInfo, password);
        } catch (NumberFormatException exception) {
            infoR.setText("Niepoprawny port");
        }
    }

    public void resetPassword() {

    }

    private void login(boolean guest) {
        try {
            String address = serverAddressTF.getText();
            int port = Integer.parseInt(serverPortTF.getText());
            if (port < 0 || port > 65535) throw new NumberFormatException();
            String login = loginL.getText();
            byte[] password = hash(passwordL.getText());

            threadClient.sendLoginRequest(address, port, login, password, guest);
        } catch (NumberFormatException exception) {
            infoL.setText("Niepoprawny port");
        }
    }

    private byte[] hash(String text) {
        text = text.trim();
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        byte[] hashed = messageDigest.digest(bytes);

        System.out.println(bytes);
        System.out.println(Arrays.toString(bytes));
        System.out.println(hashed);
        System.out.println(Arrays.toString(hashed));
        return hashed;
    }


    public void setInfoLText(String text) {
        infoL.setText(text);
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    public void setThreadClient(ThreadClient threadClient) {
        this.threadClient = threadClient;
    }
}
