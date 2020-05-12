package com.lukire.hexgame.game.graph;

public class Path {

    private int length;
    private Node from;
    private Node to;

    public Path(Node from, Node to, int length) {
        this.from=from;
        this.to=to;
        this.length=length;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Node getFrom() {
        return from;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
    }
}
