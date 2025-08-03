package comp1322.part1;

public class GameBoard {
    private int size;
    private String[][] board;
    private String currentPlayer;

    public GameBoard(int size) {
        this.size = size;
        this.board = new String[size][size];
        this.currentPlayer = "X";
        resetBoard();
    }

    public void resetBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = "";
            }
        }
    }

    public void switchPlayer() {
        currentPlayer = currentPlayer.equals("X") ? "O" : "X";
    }

    public boolean makeMove(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size || !board[row][col].isEmpty()) {
            return false; // Invalid move
        }
        board[row][col] = currentPlayer;
        return true;
    }

    public String checkWinner() {
        int winCondition = size > 4 ? size - 2 : size;
    
        // Check rows
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <= size - winCondition; j++) {
                if (checkLine(board[i], j, winCondition)) {
                    return board[i][j];
                }
            }
        }
    
        // Check columns
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <= size - winCondition; j++) {
                if (checkColumn(j, i, winCondition)) {
                    return board[j][i];
                }
            }
        }
    
        // Check diagonals
        for (int i = 0; i <= size - winCondition; i++) {
            for (int j = 0; j <= size - winCondition; j++) {
                if (checkDiagonal(i, j, winCondition)) {
                    return board[i][j];
                }
            }
        }
    
        return ""; // No winner
    }
    
    private boolean checkLine(String[] row, int start, int length) {
        String first = row[start];
        if (first.isEmpty()) return false;
        for (int i = 1; i < length; i++) {
            if (!row[start + i].equals(first)) return false;
        }
        return true;
    }
    
    private boolean checkColumn(int startRow, int col, int length) {
        String first = board[startRow][col];
        if (first.isEmpty()) return false;
        for (int i = 1; i < length; i++) {
            if (!board[startRow + i][col].equals(first)) return false;
        }
        return true;
    }
    
    private boolean checkDiagonal(int startRow, int startCol, int length) {
        String first = board[startRow][startCol];
        if (first.isEmpty()) return false;
    
        // Check main diagonal (top-left to bottom-right)
        boolean mainDiagonal = true;
        for (int i = 0; i < length; i++) {
            if (startRow + i >= size || startCol + i >= size || !board[startRow + i][startCol + i].equals(first)) {
                mainDiagonal = false;
                break;
            }
        }
    
        // Check anti-diagonal (top-right to bottom-left)
        boolean antiDiagonal = true;
        for (int i = 0; i < length; i++) {
            if (startRow + i >= size || startCol + length - i - 1 < 0 || !board[startRow + i][startCol + length - i - 1].equals(first)) {
                antiDiagonal = false;
                break;
            }
        }
    
        return mainDiagonal || antiDiagonal;
    }

    private boolean checkLine(String a, String b, String c) {
        return !a.isEmpty() && a.equals(b) && b.equals(c);
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public String[][] getBoard() {
        return board;
    }
}