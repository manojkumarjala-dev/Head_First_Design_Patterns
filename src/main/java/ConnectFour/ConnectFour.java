package ConnectFour;

import java.util.Scanner;

public class ConnectFour {
    public enum Slot{
        RED,
        YELLOW,
        EMPTY
    }

    public static class Player{
        Slot color;

        Player(Slot color){
            this.color = color;
        }

    }

    public static class Board{
        private final int rows, cols;
        private final Slot[][] grid;
        public Board(int rows, int cols) {
            this.rows = rows; this.cols = cols;
            this.grid = new Slot[rows][cols];
            for (int r = 0; r < rows; r++)
                for (int c = 0; c < cols; c++)
                    grid[r][c] = Slot.EMPTY;
        }
        public int getRows() { return rows; }
        public int getCols() { return cols; }
        public Slot at(int r, int c) { return grid[r][c]; }

        // gravity: returns landing row or -1 if column full
        public int drop(int col, Slot s) {
            if (col < 0 || col >= cols) return -1;
            for (int r = rows - 1; r >= 0; r--) {
                if (grid[r][col] == Slot.EMPTY) {
                    grid[r][col] = s;
                    return r;
                }
            }
            return -1;
        }
    }

    public static class Game{
        private final Board board;
        private final Player p1;
        private final Player p2;
        private Player current;
        private Player winner;
        private int moves;

       Game(int rows, int cols, Player player1, Player player2){
           this.board = new Board(rows, cols);
           this.p2 = player2;
           this.p1 = player1;
           this.current = player1;
       }

        public boolean makeMove(int col) {
            if (winner != null) return false; // already over
            int row = board.drop(col, current.color);
            if (row == -1) return false;     // invalid move
            moves++;
            if (checkWin(row, col, current.color)) {
                winner = current;
            } else if (moves == board.getRows() * board.getCols()) {
                winner = null; // draw; distinguish via isDraw()
            } else {
                current = (current == p1) ? p2 : p1;
            }
            return true;
        }

        public Player getWinner() { return winner; }
        public boolean isOver() { return winner != null || moves == board.getRows() * board.getCols(); }
        public boolean isDraw() { return winner == null && isOver(); }
        public Player getCurrent() { return current; }
        public Board getBoard() { return board; }

        private boolean checkWin(int r, int c, Slot s) {
            return count(r,c, 0,1,s) + count(r,c, 0,-1,s) - 1 >= 4 || // horizontal
                    count(r,c, 1,0,s) + count(r,c,-1, 0,s) - 1 >= 4 || // vertical
                    count(r,c, 1,1,s) + count(r,c,-1,-1,s) - 1 >= 4 || // diag down-right
                    count(r,c, 1,-1,s) + count(r,c,-1, 1,s) - 1 >= 4;  // diag down-left
        }
        private int count(int r, int c, int dr, int dc, Slot s) {
            int rows = board.getRows(), cols = board.getCols(), k = 0;
            while (r >= 0 && r < rows && c >= 0 && c < cols && board.at(r,c) == s) {
                k++; r += dr; c += dc;
            }
            return k;
        }

        public String getBoardString() {
            StringBuilder sb = new StringBuilder();
            for (int r = 0; r < board.getRows(); r++) {
                for (int c = 0; c < board.getCols(); c++) {
                    switch (board.at(r, c)) {
                        case RED: sb.append("R "); break;
                        case YELLOW: sb.append("Y "); break;
                        case EMPTY: sb.append(". "); break;
                    }
                }
                sb.append("\n");
            }
            return sb.toString();
        }

    }

    public static final class Series {
        private final Player p1, p2;
        private final int targetWins;
        private int s1, s2;
        public Series(Player p1, Player p2, int targetWins) {
            this.p1 = p1; this.p2 = p2; this.targetWins = targetWins;
        }
        public void record(Game g) {
            if (g.getWinner() == p1) s1++;
            else if (g.getWinner() == p2) s2++;
        }
        public boolean isOver() { return s1 == targetWins || s2 == targetWins; }
        public Player getChampion() { return s1 == targetWins ? p1 : (s2 == targetWins ? p2 : null); }
        public int getScore(Player p) {
            if (p == p1) return s1;
            if (p == p2) return s2;
            return 0;
        }

    }




    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

// Create players
        Player p1 = new Player(Slot.RED);
        Player p2 = new Player(Slot.YELLOW);

// Create series: first to 3 wins
        Series series = new Series(p1, p2, 1);

        while (!series.isOver()) {
            Game game = new Game(6, 7, p1, p2);

            while (!game.isOver()) {
                System.out.println(game.getBoardString());
                System.out.println("Player " + game.getCurrent().color + ", choose column (0-" + (game.getBoard().getCols() - 1) + "): ");
                int col = sc.nextInt();

                if (!game.makeMove(col)) {
                    System.out.println("Invalid move. Try again.");
                }
            }

            System.out.println(game.getBoardString());

            if (game.isDraw()) {
                System.out.println("It's a draw!");
            } else {
                System.out.println("Winner: " + game.getWinner().color);
            }

            series.record(game);
            System.out.println("Series Score -> " + p1.color + ": " + series.getScore(p1) + " | " + p2.color + ": " + series.getScore(p2));
        }

        assert series.getChampion() != null;
        System.out.println("Series Champion: " + series.getChampion().color);
        sc.close();

    }
}
