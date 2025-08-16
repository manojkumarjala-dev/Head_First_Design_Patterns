package SnakeAndFood;

import java.util.*;

/*
Key Components:
Game contains Board, Player
Board contains Snake, Food
Player
Snake
Food
 */
import java.util.*;

public class SnakeAndFood {
    public static void main(String[] args) {
        int rows = 10, cols = 10;
        Scanner sc = new Scanner(System.in);
        Game game = new Game(rows, cols, new Cell(0, 0), sc);
        int score = game.startGame();
        System.out.println("Final score: " + score);
        sc.close();
    }
}

enum GameStatus { IN_PROCESS, ENDED }
enum Direction { UP, DOWN, LEFT, RIGHT }

class Game {
    final Board board;
    final Snake snake;
    final Player player;
    int score;
    GameStatus gameStatus;
    final Food foodGenerator;
    Cell currentFood;

    Game(int rows, int cols, Cell startPosition, Scanner scanner) {
        this.board = new Board(rows, cols);
        this.snake = new Snake(startPosition);
        this.player = new Player(scanner);
        this.score = 0;
        this.gameStatus = GameStatus.IN_PROCESS;
        this.foodGenerator = new Food(rows, cols);
        this.currentFood = foodGenerator.generateRandomFood(snake.snakePositionSet);
    }

    int startGame() {
        while (gameStatus == GameStatus.IN_PROCESS) {
            printState();
            Cell head = snake.snakeBody.peekFirst();
            Cell next = player.move(head, snake.direction);
            // update intended direction chosen by player
            snake.direction = player.getLastDirection();

            // wall collision
            if (outOfBounds(next)) {
                gameStatus = GameStatus.ENDED;
                System.out.println("Game Over! Hit the wall.");
                break;
            }
            // self collision (stepping into tail is allowed if tail moves)
            if (snake.bitesItself(next)) {
                gameStatus = GameStatus.ENDED;
                System.out.println("Game Over! Bit yourself.");
                break;
            }

            // eat or move
            boolean grow = next.equals(currentFood);
            snake.step(next, grow);
            if (grow) {
                score++;
                currentFood = foodGenerator.generateRandomFood(snake.snakePositionSet);
                if (currentFood == null) { // board full
                    System.out.println("You filled the board. You win!");
                    gameStatus = GameStatus.ENDED;
                }
            }
        }
        return score;
    }

    private boolean outOfBounds(Cell c) {
        return c.row() < 0 || c.col() < 0 || c.row() >= board.grid.length || c.col() >= board.grid[0].length;
    }

    private void printState() {
        int R = board.grid.length, C = board.grid[0].length;
        char[][] view = new char[R][C];
        for (int r = 0; r < R; r++) Arrays.fill(view[r], '.');
        // draw food
        if (currentFood != null) view[currentFood.row()][currentFood.col()] = 'F';
        // draw snake
        boolean first = true;
        for (Cell c : snake.snakeBody) {
            view[c.row()][c.col()] = first ? 'H' : 'o';
            first = false;
        }
        System.out.println("Score: " + score + "   Use W A S D then Enter");
        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) System.out.print(view[r][c] + " ");
            System.out.println();
        }
    }
}

class Board {
    final Cell[][] grid;
    Board(int rows, int cols) {
        grid = new Cell[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                grid[i][j] = new Cell(i, j);
    }
}

record Cell(int row, int col) {}

class Player implements MoveStrategy {
    private final Scanner scanner;
    private Direction lastDirection = Direction.RIGHT;

    Player(Scanner scanner) { this.scanner = scanner; }

    @Override
    public Cell move(Cell head, Direction currentDirection) {
        System.out.print("Enter direction (W/A/S/D): ");
        String s = scanner.next().trim().toUpperCase();
        Direction dir = switch (s) {
            case "W" -> Direction.UP;
            case "S" -> Direction.DOWN;
            case "A" -> Direction.LEFT;
            case "D" -> Direction.RIGHT;
            default   -> currentDirection;
        };
        // prevent 180-degree immediate reversal if you want:
        if ((currentDirection == Direction.UP && dir == Direction.DOWN) ||
                (currentDirection == Direction.DOWN && dir == Direction.UP) ||
                (currentDirection == Direction.LEFT && dir == Direction.RIGHT) ||
                (currentDirection == Direction.RIGHT && dir == Direction.LEFT)) {
            dir = currentDirection;
        }
        lastDirection = dir;
        return switch (dir) {
            case UP    -> new Cell(head.row() - 1, head.col());
            case DOWN  -> new Cell(head.row() + 1, head.col());
            case LEFT  -> new Cell(head.row(), head.col() - 1);
            case RIGHT -> new Cell(head.row(), head.col() + 1);
        };
    }

    Direction getLastDirection() { return lastDirection; }
}

interface MoveStrategy {
    Cell move(Cell head, Direction currentDirection);
}

class Snake {
    final Deque<Cell> snakeBody = new ArrayDeque<>();
    final Set<Cell> snakePositionSet = new HashSet<>();
    Direction direction = Direction.RIGHT;

    Snake(Cell startPosition) {
        snakeBody.addFirst(startPosition);
        snakePositionSet.add(startPosition);
    }

    boolean bitesItself(Cell position) {
        Cell tail = snakeBody.getLast();
        if (position.equals(tail)) return false; // allowed if tail will move
        return snakePositionSet.contains(position);
    }

    void step(Cell next, boolean grow) {
        snakeBody.addFirst(next);
        snakePositionSet.add(next);
        if (!grow) {
            Cell t = snakeBody.removeLast();
            snakePositionSet.remove(t);
        }
    }
}

class Food {
    private final int rows, cols;
    private final Random rnd = new Random();
    Food(int rows, int cols) { this.rows = rows; this.cols = cols; }

    // place on any unoccupied cell; null if board full
    Cell generateRandomFood(Set<Cell> occupied) {
        if (occupied.size() >= rows * cols) return null;
        while (true) {
            int r = rnd.nextInt(rows), c = rnd.nextInt(cols);
            Cell candidate = new Cell(r, c);
            if (!occupied.contains(candidate)) return candidate;
        }
    }
}



/*
Low-Level Design: Snake & Food Game 🐍🍏
The Snake & Food game is a classic arcade game where the player controls a snake moving on a grid, attempting to eat food items while avoiding collisions. The game is simple in concept but requires strategic movement to maximize the snake's growth without running into walls or itself.

Rules of the Game :
Setup :
• The game is played on an N x N grid.

The snake starts at an initial position with a small length.

• The player controls the movement of the snake using directional inputs (Up, Down, Left, Right).

Game Mechanics :
• The snake moves one step at a time in the chosen direction.

• When the snake eats a food item, it grows in length, and a new food item spawns at a random position on the grid.

• The game continues until the snake collides with the wall or itself, at which point the game ends.

Game Over Conditions :
• The game ends if the snake collides with the boundary of the grid.

• The game also ends if the snake collides with itself (i.e., its own body).


Understanding:
- Snake and random position Food population
- Snake eats to increase size
- Game ends when snake collides borders of collide with itself
- The player controls the snake's movement direction.


To clarify, the key requirements are:
• A game grid of specific width and height.
• A single snake entity, controlled by the player.
• Food objects that appear at predefined positions on the grid.
• Movement mechanics, updating the game state with each move.
• Collision detection for both walls and snake's body.
• Scoring mechanism based on food consumption.




 */