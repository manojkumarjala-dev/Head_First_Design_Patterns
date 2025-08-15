package TicTacToeGame;

import java.util.Scanner;

public class TicTacToeGame {
    public enum Symbol { O, X, EMPTY }
    public enum Status { IN_PROGRESS, ENDED }

    public static class Player {
        final Symbol symbol;
        Player(Symbol symbol) { this.symbol = symbol; }
    }

    public static class Board {
        final int rows, cols;
        final Symbol[][] grid;
        Board(int rows, int cols) {
            this.rows = rows;
            this.cols = cols;
            grid = new Symbol[rows][cols];
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    grid[r][c] = Symbol.EMPTY;
                }
            }
        }
        boolean makeMove(int row, int col, Symbol s) {
            if (row < 0 || row >= rows || col < 0 || col >= cols) return false;
            if (grid[row][col] != Symbol.EMPTY) return false;
            grid[row][col] = s;
            return true;
        }
        void printBoard() {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    switch (grid[r][c]) {
                        case O: System.out.print("O "); break;
                        case X: System.out.print("X "); break;
                        default: System.out.print(". "); break;
                    }
                }
                System.out.println();
            }
        }
    }

    public static class Game {
        private final Board board;
        private final Player p1, p2;
        private Player current;
        private int moves = 0;
        private int lastRow = -1, lastCol = -1;

        Game(Player p1, Player p2, int rows, int cols) {
            this.p1 = p1;
            this.p2 = p2;
            this.current = p1;
            this.board = new Board(rows, cols);
        }

        boolean takeMove(int row, int col) {
            boolean ok = board.makeMove(row, col, current.symbol);
            if (ok) { lastRow = row; lastCol = col; moves++; }
            return ok;
        }

        Player checkWinner() {
            if (lastRow == -1) return null;
            Symbol s = board.grid[lastRow][lastCol];
            if (s == Symbol.EMPTY) return null;

            // Row
            boolean rowWin = true;
            for (int c = 0; c < board.cols; c++) {
                if (board.grid[lastRow][c] != s) { rowWin = false; break; }
            }
            Player player = s == p1.symbol ? p1 : p2;
            if (rowWin) return player;

            // Column
            boolean colWin = true;
            for (int r = 0; r < board.rows; r++) {
                if (board.grid[r][lastCol] != s) { colWin = false; break; }
            }
            if (colWin) return player;

            // Main diagonal
            if (lastRow == lastCol) {
                boolean diagWin = true;
                for (int i = 0; i < board.rows; i++) {
                    if (board.grid[i][i] != s) { diagWin = false; break; }
                }
                if (diagWin) return player;
            }

            // Anti-diagonal
            if (lastRow + lastCol == board.cols - 1) {
                boolean diagWin = true;
                for (int i = 0; i < board.rows; i++) {
                    if (board.grid[i][board.cols - 1 - i] != s) { diagWin = false; break; }
                }
                if (diagWin) return player;
            }

            return null;
        }

        boolean isDraw() { return moves == board.rows * board.cols; }
        void switchTurn() { current = (current == p1) ? p2 : p1; }
        Player getCurrent() { return current; }
        Board getBoard() { return board; }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Player p1 = new Player(Symbol.O);
        Player p2 = new Player(Symbol.X);
        Game game = new Game(p1, p2, 3, 3);

        Status status = Status.IN_PROGRESS;

        while (status == Status.IN_PROGRESS) {
            game.getBoard().printBoard();
            System.out.println("Turn: " + game.getCurrent().symbol + ". Enter row and col:");
            int row = sc.nextInt(), col = sc.nextInt();

            if (!game.takeMove(row, col)) {
                System.out.println("Invalid move. Try again.");
                continue;
            }

            Player winner = game.checkWinner();
            if (winner != null) {
                game.getBoard().printBoard();
                System.out.println("Winner is " + winner.symbol);
                status = Status.ENDED;
            } else if (game.isDraw()) {
                game.getBoard().printBoard();
                System.out.println("Game drawn");
                status = Status.ENDED;
            } else {
                game.switchTurn();
            }
        }
        sc.close();
    }
}
