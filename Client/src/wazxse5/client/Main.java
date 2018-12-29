package wazxse5.client;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ThreadClient threadClient = new ThreadClient();
        ViewManager viewManager = new ViewManager(primaryStage, threadClient);
        threadClient.setViewManager(viewManager);

        viewManager.loadInitScene();
        primaryStage.show();
    }
}
