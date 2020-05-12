package com.lukire.hexgame.game;

import com.lukire.hexgame.board.SlotState;

public class Move {


    private int x;
    private int y;
    private SlotState state;
    public Move(int x, int y, SlotState state) {
        this.x=x;
        this.y=y;
        this.state=state;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public SlotState getState() {
        return state;
    }
}
