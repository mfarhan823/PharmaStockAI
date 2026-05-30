package pharmastock.ui;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {


    public static Stage parentStage;

    @Override
    public void start(Stage primaryStage) {
        parentStage = primaryStage;
        LoginWindow login = new LoginWindow();
        login.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}