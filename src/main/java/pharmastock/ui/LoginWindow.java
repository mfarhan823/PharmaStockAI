package pharmastock.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import pharmastock.service.ConfigLoader;

public class LoginWindow {

    // Credentials loaded from config.properties — not hardcoded in source code
    private static final String ADMIN_USER = ConfigLoader.getAdminUsername();
    private static final String ADMIN_PASS = ConfigLoader.getAdminPassword();
    private static final String USER_USER  = ConfigLoader.getUserUsername();
    private static final String USER_PASS  = ConfigLoader.getUserPassword();

    public void show(Stage loginStage) {

        Label title = new Label("PharmaStock");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Please login to continue");
        subtitle.setFont(Font.font("Georgia", 13));
        subtitle.setTextFill(Color.web("#CBD5E1"));

        VBox headerBox = new VBox(4, title, subtitle);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(24, 20, 24, 20));
        headerBox.setStyle("-fx-background-color: #1E3A5F;");

        String fieldStyle =
                "-fx-background-color: #F8FAFC;" +
                        "-fx-border-color: #CBD5E1;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 7 10 7 10;";

        TextField txtUsername = new TextField();
        txtUsername.setPromptText("Enter username");
        txtUsername.setStyle(fieldStyle);
        txtUsername.setPrefWidth(260);

        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Enter password");
        txtPassword.setStyle(fieldStyle);
        txtPassword.setPrefWidth(260);

        Label lblError = new Label("");
        lblError.setFont(Font.font("Arial", 12));
        lblError.setTextFill(Color.web("#DC2626"));

        Button btnLogin = new Button("Login");
        btnLogin.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        btnLogin.setTextFill(Color.WHITE);
        btnLogin.setPrefWidth(260);
        String base  = "-fx-background-color: #1E3A5F; -fx-background-radius: 5; -fx-padding: 9 16 9 16; -fx-cursor: hand;";
        String hover = "-fx-background-color: #2D5282; -fx-background-radius: 5; -fx-padding: 9 16 9 16; -fx-cursor: hand;";
        btnLogin.setStyle(base);
        btnLogin.setOnMouseEntered(e -> btnLogin.setStyle(hover));
        btnLogin.setOnMouseExited(e  -> btnLogin.setStyle(base));

        VBox formBox = new VBox(14,
                new Label("Username:"),
                txtUsername,
                new Label("Password:"),
                txtPassword,
                lblError,
                btnLogin
        );
        formBox.setPadding(new Insets(24, 30, 24, 30));
        formBox.setAlignment(Pos.CENTER_LEFT);
        formBox.setStyle("-fx-background-color: #FFFFFF;");

        btnLogin.setOnAction(e -> {
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                lblError.setText("⚠  Please enter username and password.");
                return;
            }

            if (username.equals(ADMIN_USER) && password.equals(ADMIN_PASS)) {
                loginStage.close();
                new MainWindow("admin");

            } else if (username.equals(USER_USER) && password.equals(USER_PASS)) {
                loginStage.close();
                new MainWindow("user");

            } else {
                lblError.setText("⚠  Incorrect username or password.");
            }
        });

        VBox root = new VBox(0, headerBox, formBox);
        Scene scene = new Scene(root, 360, 400);
        loginStage.setTitle("PharmaStock - Login");
        loginStage.setScene(scene);
        loginStage.setResizable(false);
        loginStage.show();
    }
}