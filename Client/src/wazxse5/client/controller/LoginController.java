package wazxse5.client.controller;

import exception.NameIsInUseException;
import exception.NoSuchUserException;
import exception.WrongPasswordException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import wazxse5.client.ThreadClient;

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
                String login = loginTF.getText();
                String password = passwordTF.getText();

                ThreadClient threadClient = new ThreadClient(serverAddress, serverPort);
                boolean connected = threadClient.connect(login, password, guest);
                if (connected) {
                    threadClient.start();
                    loadMainScreen(threadClient);
                }
            } catch (ExecutionException e) {
                if (e.getCause() instanceof NameIsInUseException)
                    infoLabel.setText("Nazwa użytkownika jest w użyciu");
                else if (e.getCause() instanceof NoSuchUserException)
                    infoLabel.setText("Nie ma takiego użytkownika");
                else if (e.getCause() instanceof WrongPasswordException)
                    infoLabel.setText("Nieprawidłowe hasło");
                else infoLabel.setText("Nie można nawiązać połączenia");
            } catch (IOException | InterruptedException | TimeoutException e) {
                infoLabel.setText("Nie można nawiązać połączenia");
            }

        } catch (NumberFormatException exception) {
            infoLabel.setText("Niepoprawny port");
        }
    }

    private void loadMainScreen(ThreadClient threadClient) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Parent parent = loader.load();
            MainController mainController = loader.getController();
            mainController.setPrimaryStage(primaryStage);
            mainController.setThreadClient(threadClient);

            Scene scene = new Scene(parent);
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.setOnCloseRequest(event -> threadClient.close());
        } catch (IOException e) {
            infoLabel.setText("Nie można wczytać widoku okna");
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
