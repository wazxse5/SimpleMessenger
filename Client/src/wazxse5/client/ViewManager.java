package wazxse5.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import wazxse5.client.controller.LoginController;
import wazxse5.client.controller.MainController;
import wazxse5.common.exception.LoginIsNotAvailableException;
import wazxse5.common.exception.LoginNotExistsException;
import wazxse5.common.exception.NoConnectionException;
import wazxse5.common.exception.WrongPasswordException;

import java.io.IOException;

public class ViewManager {
    private Stage stage;
    private ThreadClient threadClient;

    private Scene mainScene;
    private Scene loginScene;
    private MainController mainController;
    private LoginController loginController;

    public ViewManager(Stage stage, ThreadClient threadClient) {
        this.stage = stage;
        this.threadClient = threadClient;
        this.stage.setOnCloseRequest(event -> threadClient.close());
    }

    public void loadMainScene() {
        if (mainScene == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
                mainScene = new Scene(loader.load());
                mainController = loader.getController();
                mainController.setThreadClient(threadClient);
                mainController.setViewManager(this);
            } catch (IOException e) {
                System.err.println("Nie można wczytać main.fxml");
            }
        }
        stage.setScene(mainScene);
    }

    public void loadLoginScene() {
        if (loginScene == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
                loginScene = new Scene(loader.load());
                loginController = loader.getController();
                loginController.setThreadClient(threadClient);
                loginController.setViewManager(this);
            } catch (IOException e) {
                System.err.println("Nie można wczytać login.fxml");
            }
        }
        stage.setScene(loginScene);
    }

    public void handleLoginError(Throwable throwable) {
        if (throwable instanceof LoginIsNotAvailableException)
            loginController.setInfoText("L", "Ten login jest zajęty");
        else if (throwable instanceof LoginNotExistsException)
            loginController.setInfoText("L", "Nie ma takiego użytkownika");
        else if (throwable instanceof WrongPasswordException) loginController.setInfoText("L", "Nieprawidłowe hasło");
        else loginController.setInfoText("L", "Nie można nawiązać połączenia");
    }

    public void handleConnectError(Throwable throwable) {
        if (throwable instanceof NoConnectionException)
            loginController.setInfoText("C", "Nie można nawiązać połączenia");
    }

    public void handleRegisterError(Throwable throwable) {
        if (throwable instanceof NoConnectionException) loginController.setInfoText("R", "Nie połączono");
    }
}
