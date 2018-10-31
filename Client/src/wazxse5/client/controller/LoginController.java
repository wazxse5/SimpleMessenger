package wazxse5.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import wazxse5.client.ThreadClient;
import wazxse5.client.exception.ConnectionException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class LoginController {
    private Stage primaryStage;

    @FXML private TextField loginTF;
    @FXML private PasswordField passwordTF;
    @FXML private TextField serverAddressTF;
    @FXML private TextField serverPortTF;
    @FXML private Label infoLabel;

    private void login(boolean guest) {
        try {
            String serverAddress = serverAddressTF.getText();
            int serverPort = Integer.parseInt(serverPortTF.getText());

            try {
                ThreadClient threadClient = new ThreadClient(serverAddress, serverPort);
                String login = loginTF.getText();
                String password = passwordTF.getText();
                if (guest) password = null;
                boolean connected = threadClient.connect(login, password);
                if (connected) {
                    threadClient.start();

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
                        Parent parent = loader.load();
                        MainController mainController = loader.getController();
                        mainController.setPrimaryStage(primaryStage);

                        mainController.setThreadClient(threadClient);

                        Scene scene = new Scene(parent);
                        primaryStage.setScene(scene);
                    } catch (IOException e) {
                        infoLabel.setText("Nie można wczytać widoku okna");
                    }
                }

            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                if (cause instanceof ConnectionException) {
                    ConnectionException connectionException = (ConnectionException) cause;
                    infoLabel.setText(connectionException.getInfo());
                } else infoLabel.setText("Nie można nawiązać połączenia");
            } catch (IOException | InterruptedException | TimeoutException e) {
                infoLabel.setText("Nie można nawiązać połączenia");
            }

        } catch (NumberFormatException exception) {
            infoLabel.setText("Niepoprawny port");
        }
    }

    public void loginAsGuest() {
        login(true);
    }

    public void loginAsUser() {
        login(false);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
