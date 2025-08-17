package SnakeAndLadder;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*
Objects:
Game
Board have Snakes, Ladders, Grid
- nextPosition(Slot cur, int val)
abstract Jump
    Snake extend Jump
    Ladder extend Jump
Player - 2

 */
class Game{

    Player player1;
    Player player2;
    Status status;
    Player curPlayer;
    Board board;

    Game(Player player1, Player player2, Board board){
        this.player1 = player1;
        this.player2 = player2;
        this.status = Status.IN_PROCESS;
        curPlayer = this.player1;
        this.board = board;
    }


    // game rules
    void startGame(){
        while(this.status.equals(Status.IN_PROCESS)){
            int val = rollDice();
            int nextPos = board.nextPos(curPlayer.pos, val);

            if(nextPos == 10){
                this.status = Status.ENDED;
                System.out.println("winner is "+curPlayer.symbol);
            }
            else{
                curPlayer.pos = nextPos;
                if(curPlayer==player1){
                    curPlayer=player2;
                }
                else{
                    curPlayer=player1;
                }
            }
        }
    }

    int rollDice(){
        Random rand = new Random();
        return rand.nextInt(6);
    }

}

enum Symbol{
    P1,
    P2
}

class Player{
    Symbol symbol;
    int pos;
    Player(Symbol symbol, int pos){
        this.symbol = symbol;
        this.pos = 0;
    }
}

class Board{
    Map<Slot, Jump> jumps;
    Slot[] positions;

    Board(int m, int n){
        positions = new Slot[m*n];
        jumps = new HashMap<>();
        initializeBoard();
    }

    void initializeBoard(){

        for(int i=0;i<positions.length;i++){
            positions[i] = new Slot(i);
        }
        // input the number of snakes

        // for each snake add start and end // validate that end<start

        // input the number of Ladder

        // for each ladder add start and end // validate that start<end

    }

    int nextPos(int curPos, int val){
        // id
        if(curPos+val<this.positions.length){
            Slot nextSlot = this.positions[curPos+val];
            if(this.jumps.containsKey(nextSlot)){
                return this.jumps.get(nextSlot).end.pos;
            }
            else{
                return nextSlot.pos;
            }
        }
        else{
            return this.positions[curPos].pos;
        }

    }
}

class Slot{
    int pos;
    Slot(int pos){
        this.pos = pos;
    }
}

enum Status{
    IN_PROCESS,
    ENDED
}

class JumpFactory{
    Jump createJump(Slot start, Slot end){
        return new Jump(start,end);
    }
}

class Jump{
    Slot start;
    Slot end;

    Jump(Slot start, Slot end){
        this.end = end;
        this.start = start;
    }
}

class Ladder extends Jump{
    Ladder(Slot start, Slot end) {
        super(start, end);
    }
}

class Snake extends Jump{
    Snake(Slot start, Slot end) {
        super(start, end);
    }
}

public class SnakeAndLadder {
}

/*
Board game(n*n), slot,  snakes & ladders, and players, Symbol enum, Dice (Random (6)),
Snake, Ladder
status: IN_PROGRESS Or ENDED
flow: all players will start at 0,0 -> n*n

<---------
--------->
<---------
--------->
<---------
--------->

Q's
1. board size m*n
2.


Objects:
Game
Board have Snakes, Ladders, Grid
- nextPosition(Slot cur, int val)
abstract Jump
    Snake extend Jump
    Ladder extend Jump
Player - 2


 */
