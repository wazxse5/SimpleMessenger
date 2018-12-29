package wazxse5.server;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ThreadServer threadServer = new ThreadServer();
        ViewManager viewManager = new ViewManager(primaryStage, threadServer);
        threadServer.setViewManager(viewManager);

        viewManager.loadInitScene();
        primaryStage.show();
    }
}
