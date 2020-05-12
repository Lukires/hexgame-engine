package com.lukire.hexgame.exceptions;

public class NoHexagonException extends Exception {

    private float x;
    private float y;
    public NoHexagonException(float x, float y) {
        this.x=x;
        this.y=y;
    }

    public String toString() {
        return String.format("NoHexagonException at X:%s Y:%s", x, y);
    }
}
