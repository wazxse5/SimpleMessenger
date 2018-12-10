package wazxse5.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import wazxse5.client.controller.LoginController;
import wazxse5.client.controller.MainController;

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
        stage.setOnCloseRequest(event -> threadClient.close());
    }

    public void loadMainScene() throws IOException {
        if (mainScene == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            mainScene = new Scene(loader.load());
            mainController = loader.getController();
            mainController.setThreadClient(threadClient);
            mainController.setViewManager(this);
        }
        stage.setScene(mainScene);
    }

    public void loadLoginScene() throws IOException {
        if (loginScene == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            loginScene = new Scene(loader.load());
            loginController = loader.getController();
            loginController.setThreadClient(threadClient);
            loginController.setViewManager(this);
        }
        stage.setScene(loginScene);
    }
}
