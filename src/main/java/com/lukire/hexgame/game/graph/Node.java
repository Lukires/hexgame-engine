package com.lukire.hexgame.game.graph;

import com.lukire.hexgame.board.SlotState;

import java.util.ArrayList;
import java.util.HashMap;

public class Node {

    protected int x;
    protected int y;
    protected SlotState state;


    public Node(int x, int y, SlotState state) {
        this.x=x;
        this.state=state;
        this.y=y;
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

    public void setState(SlotState state) {
        this.state=state;
    }

}
