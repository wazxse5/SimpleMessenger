package wazxse5.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import wazxse5.client.controller.*;
import wazxse5.common.exception.*;
import wazxse5.common.message.UserMessage;
import wazxse5.common.message.config.GoodbyeMessage;

import java.io.IOException;

public class ViewManager {
    private Stage stage;
    private ThreadClient threadClient;

    private Scene mainScene;
    private Scene initScene;
    private Node loginNode;
    private Node registerNode;
    private Node resetPasswordNode;
    private MainController mainController;
    private InitController initController;
    private LoginController loginController;
    private RegisterController registerController;
    private ResetPasswordController resetPasswordController;

    public ViewManager(Stage stage, ThreadClient threadClient) {
        this.stage = stage;
        this.threadClient = threadClient;
        this.stage.setMinWidth(400);
        this.stage.setMinHeight(300);
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
        loadLoginRegisterResetNodes();
        stage.setScene(initScene);
        stage.setTitle("Simple Messenger by wazxse5");
    }

    private void loadLoginRegisterResetNodes() {
        try {
            FXMLLoader loaderLogin = new FXMLLoader(getClass().getResource("/login.fxml"));
            FXMLLoader loaderRegister = new FXMLLoader(getClass().getResource("/register.fxml"));
            FXMLLoader loaderResetPassword = new FXMLLoader(getClass().getResource("/resetPassword.fxml"));

            loginNode = loaderLogin.load();
            registerNode = loaderRegister.load();
            resetPasswordNode = loaderResetPassword.load();

            loginController = loaderLogin.getController();
            registerController = loaderRegister.getController();
            resetPasswordController = loaderResetPassword.getController();
            loginController.setThreadClient(threadClient);
            registerController.setThreadClient(threadClient);
            resetPasswordController.setThreadClient(threadClient);
            loginController.setViewManager(this);
            registerController.setViewManager(this);
            resetPasswordController.setViewManager(this);
        } catch (IOException e) {
            System.err.println("Nie można wczytać login/register/reset.fxml");
        }
        initController.getLoginTab().setContent(loginNode);
        initController.getRegisterTab().setContent(registerNode);
        initController.getResetPasswordTab().setContent(resetPasswordNode);
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
            loginController.setInfoText("Ten login jest zajęty");
        else if (throwable instanceof LoginNotExistsException)
            loginController.setInfoText("Nie ma takiego użytkownika");
        else if (throwable instanceof WrongPasswordException) loginController.setInfoText("Nieprawidłowe hasło");
        else loginController.setInfoText("Nie można nawiązać połączenia");
    }

    public void handleConnectError(Throwable throwable) {
        if (throwable instanceof DatabaseException)
            initController.setInfoText("Nie można nawiązać połączenia");
    }

    public void handleRegisterError(Throwable throwable) {
        if (throwable == null) registerController.setInfoText("Zarejestrowano");
        else if (throwable instanceof NoConnectionException) registerController.setInfoText("Nie połączono");
        else if (throwable instanceof DatabaseException) registerController.setInfoText("Błąd bazy danych");
    }

    public void handleReceivedUserMessage(UserMessage userMessage) {
        mainController.handleReceivedMessage(userMessage.getFrom(), userMessage.getMessage());
    }

    public void handleReceivedGoodbyeMessage(GoodbyeMessage goodbyeMessage) {
        if (goodbyeMessage.getMessage().equals("exit")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ostrzeżenie");
            alert.setHeaderText(null);
            alert.setContentText("Serwer rozłączył się");
            alert.showAndWait();
        }
    }

    public InitController getInitController() {
        return initController;
    }
}
