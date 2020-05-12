package com.lukire.hexgame.exceptions;

public class IllegalMoveException extends Exception {
    private int  x;
    private int y;
    public IllegalMoveException(int x, int y) {
        this.x=x;
        this.y=y;
    }

    public String toString() {
        return String.format("IllegalMoveException at X:%s Y:%s", x, y);
    }
}
