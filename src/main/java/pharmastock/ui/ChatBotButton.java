package pharmastock.ui;

import pharmastock.service.ChatBot;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ChatBotButton {
    private Stage mainStage;

    private ChatBot chatBot;
    private Stage   buttonStage;
    private Stage   chatStage;
    private VBox    chatMessages;
    private boolean isOpen = false;


    public ChatBotButton(ChatBot chatBot, Stage mainStage) {
        this.chatBot = chatBot;
        this.mainStage = mainStage; // save
        buildFloatingButton(mainStage);
    }
    private void buildFloatingButton(Stage mainStage) {

        buttonStage = new Stage();
        buttonStage.initStyle(StageStyle.TRANSPARENT);
        buttonStage.setAlwaysOnTop(true);

        Button fabButton = new Button("🤖");
        fabButton.setFont(Font.font("Arial", 22));
        fabButton.setStyle(
                "-fx-background-color: #1E3A5F;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 50;" +
                        "-fx-min-width: 55px;" +
                        "-fx-min-height: 55px;" +
                        "-fx-cursor: hand;"
        );
        fabButton.setTooltip(new Tooltip("PharmaBot — Click to chat"));

        fabButton.setOnMouseEntered(e -> fabButton.setStyle(
                "-fx-background-color: #2D5282;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 50;" +
                        "-fx-min-width: 55px;" +
                        "-fx-min-height: 55px;" +
                        "-fx-cursor: hand;"
        ));
        fabButton.setOnMouseExited(e -> fabButton.setStyle(
                "-fx-background-color: #1E3A5F;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 50;" +
                        "-fx-min-width: 55px;" +
                        "-fx-min-height: 55px;" +
                        "-fx-cursor: hand;"
        ));

        fabButton.setOnAction(e -> {
            if (isOpen) {
                closeChatWindow();
            } else {
                openChatWindow();
            }
        });

        StackPane buttonRoot = new StackPane(fabButton);
        buttonRoot.setStyle("-fx-background-color: transparent;");
        buttonRoot.setPadding(new Insets(5));

        Scene buttonScene = new Scene(buttonRoot, 70, 70);
        buttonScene.setFill(Color.TRANSPARENT);
        buttonStage.setScene(buttonScene);
        buttonStage.setX(mainStage.getX() + mainStage.getWidth() - 80);
        buttonStage.setY(mainStage.getY() + mainStage.getHeight() - 80);
        buttonStage.show();

        // Draggable
        final double[] offset = new double[2];
        buttonRoot.setOnMousePressed(e -> {
            offset[0] = e.getScreenX() - buttonStage.getX();
            offset[1] = e.getScreenY() - buttonStage.getY();
        });
        buttonRoot.setOnMouseDragged(e -> {
            buttonStage.setX(e.getScreenX() - offset[0]);
            buttonStage.setY(e.getScreenY() - offset[1]);
        });

        // Main window band ho toh dono band ho jayen
        mainStage.setOnCloseRequest(e -> {
            if (chatStage != null) chatStage.close();
            buttonStage.close();
        });
    }

    private void openChatWindow() {
        isOpen = true;
        chatStage = new Stage();
        chatStage.initStyle(StageStyle.TRANSPARENT);
        chatStage.setAlwaysOnTop(true);

        Label botTitle    = new Label("🤖  PharmaBot");
        Label botSubtitle = new Label("Powered by Grok AI");
        botTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        botTitle.setTextFill(Color.WHITE);
        botSubtitle.setFont(Font.font("Arial", 11));
        botSubtitle.setTextFill(Color.web("#CBD5E1"));

        VBox titleBox = new VBox(2, botTitle, botSubtitle);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Button btnClose = new Button("✕");
        btnClose.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 2 6 2 6;"
        );
        btnClose.setOnAction(e -> closeChatWindow());

        HBox chatHeader = new HBox(titleBox, btnClose);
        HBox.setHgrow(titleBox, Priority.ALWAYS);
        chatHeader.setPadding(new Insets(12, 14, 12, 14));
        chatHeader.setAlignment(Pos.CENTER_LEFT);
        chatHeader.setStyle("-fx-background-color: #1E3A5F; -fx-background-radius: 12 12 0 0;");

        chatMessages = new VBox(8);
        chatMessages.setPadding(new Insets(12));

        ScrollPane scrollPane = new ScrollPane(chatMessages);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #F8FAFC; -fx-background: #F8FAFC;");
        scrollPane.setPrefHeight(300);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        addBotMessage("Hello! I am PharmaBot, your AI pharmacy assistant.\nYou can ask me anything about your inventory!\n\nExample questions:\n- How many medicines do we have?\n- Which medicines are low on stock?\n- What is the price of Panadol?");

        TextField txtInput = new TextField();
        txtInput.setPromptText("Ask anything...");
        txtInput.setStyle(
                "-fx-background-color: #F8FAFC;" +
                        "-fx-border-color: #CBD5E1;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-font-size: 12px;" +
                        "-fx-padding: 7 10 7 10;"
        );
        HBox.setHgrow(txtInput, Priority.ALWAYS);

        Button btnSend = new Button("Send");
        btnSend.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        btnSend.setTextFill(Color.WHITE);
        btnSend.setStyle(
                "-fx-background-color: #1E3A5F;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 7 12 7 12;" +
                        "-fx-cursor: hand;"
        );

        HBox inputRow = new HBox(8, txtInput, btnSend);
        inputRow.setPadding(new Insets(10, 12, 12, 12));
        inputRow.setAlignment(Pos.CENTER);
        inputRow.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 0 0 12 12;");

        Runnable sendAction = () -> {
            String msg = txtInput.getText().trim();
            if (msg.isEmpty()) return;

            addUserMessage(msg);
            txtInput.clear();
            btnSend.setDisable(true);
            addBotMessage("...");

            Thread t = new Thread(() -> {
                String response = chatBot.getResponse(msg);
                Platform.runLater(() -> {
                    removeLastMessage();
                    addBotMessage(response);
                    btnSend.setDisable(false);
                    scrollPane.setVvalue(1.0);
                });
            });
            t.setDaemon(true);
            t.start();
        };

        btnSend.setOnAction(e -> sendAction.run());
        txtInput.setOnAction(e -> sendAction.run());

        VBox chatRoot = new VBox(0, chatHeader, scrollPane, inputRow);
        chatRoot.setStyle(
                "-fx-background-color: #FFFFFF;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #CBD5E1;" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-width: 1;"
        );

        Scene chatScene = new Scene(chatRoot, 320, 420);
        chatScene.setFill(Color.TRANSPARENT);
        chatStage.setScene(chatScene);
        chatStage.setX(mainStage.getX() + mainStage.getWidth() - 340);
        chatStage.setY(mainStage.getY() + mainStage.getHeight() - 510);
        chatStage.show();
    }

    private void closeChatWindow() {
        isOpen = false;
        if (chatStage != null) {
            chatStage.close();
        }
    }

    private void addBotMessage(String text) {
        Label msg = new Label("🤖  " + text);
        msg.setWrapText(true);
        msg.setMaxWidth(240);
        msg.setFont(Font.font("Arial", 12));
        msg.setStyle(
                "-fx-background-color: #EBF4FF;" +
                        "-fx-text-fill: #0C447C;" +
                        "-fx-background-radius: 4 12 12 4;" +
                        "-fx-padding: 8 12 8 12;"
        );
        HBox row = new HBox(msg);
        row.setAlignment(Pos.CENTER_LEFT);
        chatMessages.getChildren().add(row);
    }

    private void addUserMessage(String text) {
        Label msg = new Label(text);
        msg.setWrapText(true);
        msg.setMaxWidth(220);
        msg.setFont(Font.font("Arial", 12));
        msg.setStyle(
                "-fx-background-color: #1E3A5F;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 12 4 4 12;" +
                        "-fx-padding: 8 12 8 12;"
        );
        HBox row = new HBox(msg);
        row.setAlignment(Pos.CENTER_RIGHT);
        chatMessages.getChildren().add(row);
    }

    private void removeLastMessage() {
        if (!chatMessages.getChildren().isEmpty()) {
            chatMessages.getChildren().remove(chatMessages.getChildren().size() - 1);
        }
    }
}