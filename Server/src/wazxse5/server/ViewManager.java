package wazxse5.server;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import wazxse5.server.controller.InitController;
import wazxse5.server.controller.MainController;

import java.io.IOException;

public class ViewManager {
    private Stage stage;
    private ThreadServer threadServer;

    private Scene initScene;
    private Scene mainScene;
    private InitController initController;
    private MainController mainController;

    public ViewManager(Stage stage, ThreadServer threadServer) {
        this.stage = stage;
        this.threadServer = threadServer;
        this.stage.setOnCloseRequest(event -> threadServer.close());
    }

    public void loadInitScene() {
        if (initScene == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/init.fxml"));
                initScene = new Scene(loader.load());
                initController = loader.getController();
                initController.setThreadServer(threadServer);
                initController.setViewManager(this);
            } catch (IOException e) {
                System.err.println("Nie można wczytać init.fxml");
            }
        }
        stage.setResizable(false);
        stage.setScene(initScene);
    }

    public void loadMainScene() {
        if (mainScene == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
                mainScene = new Scene(loader.load());
                mainController = loader.getController();
                mainController.setThreadServer(threadServer);
                mainController.setViewManager(this);
            } catch (IOException e) {
                System.err.println("Nie można wczytać main.fxml");
            }
        }
        stage.setTitle("Serwer działa - port: " + threadServer.getServerPort());
        stage.setResizable(true);
        stage.setScene(mainScene);
    }

    public void refreshConnectedConnectionsTable() {
        mainController.refreshConnectionsTable();
    }
}
