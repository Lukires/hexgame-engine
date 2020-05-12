package com.lukire.hexgame.game;

import com.lukire.hexgame.board.GameBoard;
import com.lukire.hexgame.board.SlotState;
import com.lukire.hexgame.board.ui.BoardInterface;
import com.lukire.hexgame.board.ui.Hexagon;
import com.lukire.hexgame.exceptions.IllegalMoveException;
import com.lukire.hexgame.exceptions.NoHexagonException;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.HashMap;

public class GameHandler {

    private PApplet process;
    private GameBoard board;
    private BoardInterface boardInterface;
    private ArrayList<Move> moves = new ArrayList<Move>();
    private GameState state = GameState.SELECTION;

    public boolean turn = false;

    public GameHandler(PApplet process) {
        this.process=process;
        board = new GameBoard(11,11);
        boardInterface = new BoardInterface(board, process);
    }

    public GameState getState() {
        return state;
    }

    public GameBoard getBoard() {
        return board;
    }

    public BoardInterface getBoardInterface() {
        return boardInterface;
    }

    public void setState(GameState state) {
        this.state=state;
    }

    //Click EventHandler
    public void click(float x, float y) {
        try {
            Hexagon hexagon = boardInterface.getHexagon(x,y);
            System.out.println(hexagon.toString());
            newMove(hexagon.getX(), hexagon.getY());
            hexagon.draw(process);
        }catch(Exception e) {

        }
    }

    public void newMove(int x, int y) throws IllegalMoveException {

        if (board.nodes[x][y].getState() != SlotState.EMPTY) {
            System.out.println(board.nodes[x][y].getState().toString());
            throw new IllegalMoveException(x, y);
        }
        SlotState state = turn?SlotState.RED:SlotState.BLUE;
        moves.add(new Move(x, y, state));
        board.nodes[x][y].setState(state);

        boolean win = resetCheckWin(x,y,state);
        if (win) {
            this.state = GameState.FINISHED;
        }

        turn=!turn;
    }


    private Boolean[][] visited = new Boolean[11][11];

    public boolean resetCheckWin(int x, int y, SlotState state) {
        first = false;
        second = false;
        for (int dx = 0; dx < board.getWidth(); dx++) {
            for (int dy = 0; dy < board.getHeight(); dy++) {
                visited[dx][dy] = false;
            }
        }
        return checkWin(x,y,state);
    }


    //Recursive funktion
    /*
    Går gennem et felt, og alle felter ved siden af det felt, indtil den enten støder på to af side felterne, eller til der ikke er flere felte at gå igennem.
    Looper ikke igennem tomme felter

    I stedet for at have en recursive funktion, til at tjekke om man har vundet hver tur, så kunne man i stedet gemme ens brik connected sider.
    I det at vi sætter en brik i siden, vil vi gemme dens side. Når vi så sætter en brik ved siden af sådan en brik, så vil vi overføre dens brik side til den anden.
    Ved at gemme alle brikker der er connected, og til hvilke sider de er connected, kan vi undgå en recursive funktion som denne.
    Det andet vil være hurtigere for programmet
     */

    boolean first = false, second = false;
    private boolean checkWin(int x, int y, SlotState state) {

        if (state == SlotState.BLUE) {
            if (y == -1) {
                first=true;
            }else if(y == 11) {
                second=true;
            }
        }else if(state == SlotState.RED) {
            if (x == -1) {
                first=true;
            }else if(x == 11) {
                second=true;
            }
        }

        if (first && second) {
            return true;
        }

        if (x < 0 || y < 0 || y > 10 || x > 10) {
            return false;
        }

        if (visited[x][y]) {
            return false;
        }else {
            visited[x][y] = true;
        }

        if (board.nodes[x][y].getState() != state) {
            return false;
        }

        //Alle nabo felter
        return checkWin(x, y-1, state) || checkWin(x+1, y-1, state) ||
                checkWin(x-1, y, state) || checkWin(x+1, y, state) ||
                checkWin(x-1, y+1, state) || checkWin(x, y+1, state);

    }

    public void refresh(){
        boardInterface.display();
    }


}
