package wazxse5.client.controller;

import javafx.fxml.FXML;
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
    @FXML private TextField serverAddressTF;
    @FXML private TextField serverPortTF;
    @FXML private Label infoLabel;

    public void loginAsGuest() {
        login(true);
    }

    public void loginAsUser() {
        login(false);
    }

    private void login(boolean guest) {
        try {
            String address = serverAddressTF.getText();
            int port = Integer.parseInt(serverPortTF.getText());
            String login = loginTF.getText();
            String password = passwordTF.getText();

            threadClient.connect(address, port, login, password, guest);
        } catch (NumberFormatException exception) {
            infoLabel.setText("Niepoprawny port");
        }
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    public void setThreadClient(ThreadClient threadClient) {
        this.threadClient = threadClient;
    }
}
