package ChessGame;

import java.util.Optional;



public class ChessGame {
    public static void main(String[] args){

    }
}

record Pos(int x, int y) {
}

record Move(Pos to, Pos from, Promotion promotion){

}

class Promotion{

}


enum Color{
    BLACK, WHITE
}

enum GameStatus{
    IN_PROCESS, ENDED
}

class Game{

    Player white, black; Board board; Color turn = Color.WHITE; GameStatus status = GameStatus.IN_PROCESS;

    boolean makeMove(Move m){
        // 1) validate ownership & in-bounds
        // 2) validate piece pattern (piece.legalMoves)
        // 3) simulate -> ensure my king not in check
        // 4) apply move, toggle turn, update status
        return true;
    }

}

class Player{
    Color color;
    Player(Color color){
        this.color = color;
    }
}

class Board{
    Cell[][] board;

    boolean inBounds(Pos p){ return false;  }
    boolean isEmpty(Pos p){ return false; }
    boolean isEnemy(Pos p, Color me){ return false; }

    boolean makeMove(Move m) {
        //BoundsValidator →
        //OwnershipValidator →
        //PatternValidator →
        //PathClearValidator →
        //KingSafetyValidator
        return false;
    }

}

class Cell{
    Piece piece;
    Pos pos;

    Cell(Pos pos) {
        this.pos = pos;
    }

    void setPiece(Piece piece){
        this.piece = piece;
    }

    void removePiece(){
        this.piece = null;
    }
}

abstract class Piece{

    abstract void getAllPossibleMoves(Pos pos);
    abstract boolean isValidMove(Pos currentPosition, Pos destinationPosition);

}

class Pawn extends Piece{

    void getAllPossibleMoves(Pos pos) {

    }

    @Override
    boolean isValidMove(Pos currentPosition, Pos destinationPosition) {
        return false;
    }

}

class Rook extends Piece{

    void getAllPossibleMoves(Pos pos) {
        //same row all columns check


        //same col all rows check
    }

    @Override
    boolean isValidMove(Pos currentPosition, Pos destinationPosition) {
        return false;
    }

}

class Bishop extends Piece{
    void getAllPossibleMoves(Pos pos) {
        //forward right diagonal


        //forward left diagonal


        //backward left diagonal


        //backward right diagonal
    }

    @Override
    boolean isValidMove(Pos currentPosition, Pos destinationPosition) {
        return false;
    }
}

class King extends Piece{
    void getAllPossibleMoves(Pos pos) {
        //one step in 8 directions

    }

    @Override
    boolean isValidMove(Pos currentPosition, Pos destinationPosition) {
        return false;
    }
}

class Queen extends Piece{
    void getAllPossibleMoves(Pos pos) {
        //same row all columns check


        //same col all rows check


        //forward right diagonal


        //forward left diagonal


        //backward left diagonal


        //backward right diagonal


        //one step in 8 directions
    }

    @Override
    boolean isValidMove(Pos currentPosition, Pos destinationPosition) {
        return false;
    }
}

class Knight extends Piece{
    void getAllPossibleMoves(Pos pos) {
        //1F2R


        //1F2L


        //2F1R


        //2F1L


        //2BL1


        //2BR1


        //1BL2


        //1BR2


    }

    @Override
    boolean isValidMove(Pos currentPosition, Pos destinationPosition) {
        return false;
    }
}



/*

chess = game
board
square
pieces - different types - each have different move strategy

- abstraction/Interface Moveable
2 players

clarifying q's:
should i support undo/redo







 */
