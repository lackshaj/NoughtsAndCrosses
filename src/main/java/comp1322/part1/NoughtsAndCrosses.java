package comp1322.part1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NoughtsAndCrosses extends Application {

    private static final int SIZE = 3;
    private GameBoard gameBoard = new GameBoard(SIZE);
    private Button[][] buttons = new Button[SIZE][SIZE];
    private Label messageLabel = new Label("Player X's Turn");
    private Label scoreLabel = new Label("Scores: X = 0 | O = 0 | Draws = 0");

    private int xWins = 0;
    private int oWins = 0;
    private int draws = 0;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        VBox topBox = new VBox();

        Button resetButton = new Button("Reset Game");
        resetButton.setOnAction(e -> resetGame());

        topBox.getChildren().addAll(resetButton, scoreLabel);
        root.setTop(topBox);

        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Button btn = new Button("");
                btn.setPrefSize(100, 100);
                btn.setStyle("-fx-font-size: 24px;");
                buttons[row][col] = btn;
                int finalRow = row;
                int finalCol = col;
                btn.setOnAction(e -> handleMove(btn, finalRow, finalCol));
                grid.add(btn, col, row);
            }
        }

        root.setCenter(grid);
        root.setBottom(messageLabel);
        BorderPane.setMargin(messageLabel, new javafx.geometry.Insets(10));

        Scene scene = new Scene(root, 350, 400);
        primaryStage.setTitle("Noughts and Crosses");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleMove(Button button, int row, int col) {
        if (gameBoard.makeMove(row, col)) {
            button.setText(gameBoard.getCurrentPlayer());
            String winner = gameBoard.checkWinner();
            if (!winner.isEmpty()) {
                if (winner.equals("X")) {
                    xWins++;
                } else {
                    oWins++;
                }
                messageLabel.setText("Player " + winner + " wins!");
                updateScores();
                disableAllButtons();
            } else if (isBoardFull()) {
                draws++;
                messageLabel.setText("It's a draw!");
                updateScores();
            } else {
                gameBoard.switchPlayer();
                messageLabel.setText("Player " + gameBoard.getCurrentPlayer() + "'s Turn");
            }
        }
    }

    private boolean isBoardFull() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (buttons[row][col].getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetGame() {
        gameBoard.resetBoard();
        for (Button[] row : buttons) {
            for (Button btn : row) {
                btn.setText("");
                btn.setDisable(false);
            }
        }
        messageLabel.setText("Player X's Turn");
    }

    private void disableAllButtons() {
        for (Button[] row : buttons) {
            for (Button btn : row) {
                btn.setDisable(true);
            }
        }
    }

    private void updateScores() {
        scoreLabel.setText(String.format("Scores: X = %d | O = %d | Draws = %d", xWins, oWins, draws));
    }

    public static void main(String[] args) {
        launch(args);
    }
}