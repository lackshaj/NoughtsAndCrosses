package comp1322.part1;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class NoughtsAndCrossesApp extends Application {

    private int gridSize = 3;
    private StringProperty playerXName = new SimpleStringProperty("Player X");
    private StringProperty playerOName = new SimpleStringProperty("Player O");
    private StringProperty currentPlayerMessage = new SimpleStringProperty();
    private IntegerProperty xWins = new SimpleIntegerProperty(0);
    private IntegerProperty oWins = new SimpleIntegerProperty(0);
    private IntegerProperty draws = new SimpleIntegerProperty(0);

    private GameBoard gameBoard;
    private Button[][] buttons;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLoadingScreen();
    }

    private void showLoadingScreen() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Noughts and Crosses");
        titleLabel.setStyle("-fx-font-size: 24px;");

        TextField playerXField = new TextField();
        playerXField.setPromptText("Enter Player X Name");

        TextField playerOField = new TextField();
        playerOField.setPromptText("Enter Player O Name");

        Spinner<Integer> gridSizeSpinner = new Spinner<>(3, 10, 3);
        gridSizeSpinner.setEditable(true);

        Button startButton = new Button("Start Game");
        startButton.setDisable(true);

        // Enable the start button only when both names are entered
        playerXField.textProperty().addListener((obs, oldText, newText) ->
                startButton.setDisable(playerXField.getText().isEmpty() || playerOField.getText().isEmpty()));
        playerOField.textProperty().addListener((obs, oldText, newText) ->
                startButton.setDisable(playerXField.getText().isEmpty() || playerOField.getText().isEmpty()));

        startButton.setOnAction(e -> {
            playerXName.set(playerXField.getText());
            playerOName.set(playerOField.getText());
            gridSize = gridSizeSpinner.getValue();
            startGame();
        });

        root.getChildren().addAll(titleLabel, playerXField, playerOField, new Label("Grid Size:"), gridSizeSpinner, startButton);

        Scene scene = new Scene(root, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Noughts and Crosses");
        primaryStage.show();
    }

    private void startGame() {
        gameBoard = new GameBoard(gridSize);
        buttons = new Button[gridSize][gridSize];

        BorderPane root = new BorderPane();
        VBox topBox = new VBox(5);
        topBox.setAlignment(Pos.CENTER);

        Label scoreLabel = new Label();
        scoreLabel.textProperty().bind(playerXName.concat(" vs ").concat(playerOName));

        Label messageLabel = new Label();
        messageLabel.textProperty().bind(currentPlayerMessage);
        currentPlayerMessage.set(playerXName.get() + "'s Turn");

        Button showScoreboardButton = new Button("Show Scoreboard");
        showScoreboardButton.setOnAction(e -> showScoreboard());

        topBox.getChildren().addAll(scoreLabel, messageLabel, showScoreboardButton);
        root.setTop(topBox);

        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setAlignment(Pos.CENTER);

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                Button button = new Button("");
                button.setPrefSize(100, 100);
                button.setStyle("-fx-font-size: 24px;");
                buttons[row][col] = button;
                int finalRow = row;
                int finalCol = col;
                button.setOnAction(e -> handleMove(button, finalRow, finalCol));
                grid.add(button, col, row);
            }
        }

        root.setCenter(grid);

        Scene scene = new Scene(root, 400, 450);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    private void handleMove(Button button, int row, int col) {
        if (gameBoard.makeMove(row, col)) {
            button.setText(gameBoard.getCurrentPlayer());
            String winner = gameBoard.checkWinner();
            if (!winner.isEmpty()) {
                if (winner.equals("X")) {
                    xWins.set(xWins.get() + 1);
                    showEndScreen(playerXName.get() + " Wins!");
                } else {
                    oWins.set(oWins.get() + 1);
                    showEndScreen(playerOName.get() + " Wins!");
                }
            } else if (isBoardFull()) {
                draws.set(draws.get() + 1);
                showEndScreen("It's a Draw!");
            } else {
                gameBoard.switchPlayer();
                currentPlayerMessage.set((gameBoard.getCurrentPlayer().equals("X") ? playerXName.get() : playerOName.get()) + "'s Turn");
            }
        }
    }

    private boolean isBoardFull() {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (buttons[row][col].getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void showEndScreen(String result) {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        Label resultLabel = new Label(result);
        resultLabel.setStyle("-fx-font-size: 24px;");

        Button playAgainButton = new Button("Play Again");
        playAgainButton.setOnAction(e -> startGame()); // Restart the game with the same settings

        Button returnToHomeButton = new Button("Return to Home");
        returnToHomeButton.setOnAction(e -> {
            resetScoreboard(); // Reset the scoreboard
            showLoadingScreen(); // Return to the home screen
        });

        root.getChildren().addAll(resultLabel, playAgainButton, returnToHomeButton);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
    }

    private void resetScoreboard() {
        xWins.set(0);
        oWins.set(0);
        draws.set(0);
    }

    private void showScoreboard() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        Label scoreboardLabel = new Label("Scoreboard");
        scoreboardLabel.setStyle("-fx-font-size: 24px;");

        Label xScoreLabel = new Label();
        xScoreLabel.textProperty().bind(playerXName.concat(": ").concat(xWins.asString()));

        Label oScoreLabel = new Label();
        oScoreLabel.textProperty().bind(playerOName.concat(": ").concat(oWins.asString()));

        Label drawsLabel = new Label();
        drawsLabel.textProperty().bind(new SimpleStringProperty("Draws: ").concat(draws.asString()));

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> ((Stage) closeButton.getScene().getWindow()).close());

        root.getChildren().addAll(scoreboardLabel, xScoreLabel, oScoreLabel, drawsLabel, closeButton);

        Scene scene = new Scene(root, 300, 200);
        Stage scoreboardStage = new Stage();
        scoreboardStage.setTitle("Scoreboard");
        scoreboardStage.setScene(scene);
        scoreboardStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}