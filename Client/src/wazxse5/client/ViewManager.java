package wazxse5.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import wazxse5.client.controller.InitController;
import wazxse5.client.controller.MainController;
import wazxse5.common.exception.*;
import wazxse5.common.message.UserMessage;

import java.io.IOException;

public class ViewManager {
    private Stage stage;
    private ThreadClient threadClient;

    private Scene mainScene;
    private Scene initScene;
    private MainController mainController;
    private InitController initController;

    public ViewManager(Stage stage, ThreadClient threadClient) {
        this.stage = stage;
        this.threadClient = threadClient;
        this.stage.setOnCloseRequest(event -> threadClient.close());
    }

    public void loadInitScene() {
        if (initScene == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/init.fxml"));
                initScene = new Scene(loader.load());
                initController = loader.getController();
                initController.setThreadClient(threadClient);
                initController.setViewManager(this);
            } catch (IOException e) {
                System.err.println("Nie można wczytać init.fxml");
            }
        }
        stage.setScene(initScene);
        stage.setTitle("Simple Messenger by wazxse5");
    }

    public void loadMainScene(String stageTitle) {
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
        stage.setTitle(stageTitle);
    }

    public void handleLoginError(Throwable throwable) {
        if (throwable instanceof LoginIsNotAvailableException)
            initController.setInfoText("L", "Ten login jest zajęty");
        else if (throwable instanceof LoginNotExistsException)
            initController.setInfoText("L", "Nie ma takiego użytkownika");
        else if (throwable instanceof WrongPasswordException) initController.setInfoText("L", "Nieprawidłowe hasło");
        else initController.setInfoText("L", "Nie można nawiązać połączenia");
    }

    public void handleConnectError(Throwable throwable) {
        if (throwable instanceof DatabaseException)
            initController.setInfoText("C", "Nie można nawiązać połączenia");
    }

    public void handleRegisterError(Throwable throwable) {
        if (throwable instanceof NoConnectionException) initController.setInfoText("R", "Nie połączono");
        else if (throwable instanceof DatabaseException) initController.setInfoText("R", "Błąd bazy danych");
    }

    public void handleReceivedUserMessage(UserMessage userMessage) {
        mainController.handleReceivedMessage(userMessage.getFrom(), userMessage.getMessage());
    }

    public InitController getInitController() {
        return initController;
    }
}
