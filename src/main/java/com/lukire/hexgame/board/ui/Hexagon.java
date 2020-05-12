package com.lukire.hexgame.board.ui;

import com.lukire.hexgame.board.SlotState;
import com.lukire.hexgame.game.graph.Node;
import processing.core.PApplet;
import processing.core.PVector;

public class Hexagon extends Node {

    //CONSTANT RADIUS
    private PVector center;
    public Hexagon(int x, int y, SlotState state)
    {
        super(x,y,state);
        this.state=state;
        center = new PVector(BoardInterface.radius/2+(36*x)+y*18+BoardInterface.radius/2, BoardInterface.radius/2+30*y+BoardInterface.radius/2);
    }

    public PVector getCenter() {
        return center;
    }

    public void draw(PApplet process) {
        switch(state){
            case RED:
                process.fill(255,0,0);
                break;
            case BLUE:
                process.fill(0,0,255);
                break;
            default:
                process.fill(255,255,255);
                break;
        }
        polygon(BoardInterface.radius/2+(36*x)+y*18+BoardInterface.radius/2, BoardInterface.radius/2+30*y+BoardInterface.radius/2, 20, 6, process);
    }

    public void polygon(float x, float y, float radius, int npoints, PApplet process) {
        float angle = process.TWO_PI / npoints;
        process.beginShape();
        for (float a = process.PI/2; a < process.TWO_PI+process.PI/2; a += angle) {

            process.stroke(0,0,0);
            if (this.x == 0 || this.x==10) {
                process.stroke(255, 0, 0);
            }
            if (this.y==0 || this.y==10){
                process.stroke(0,0,255);
            }
            if((this.y==0 || this.y==10) && (this.x==0 || this.x==10)) {
                process.stroke(127,0,127);
            }

            float sx = x + process.cos(a) * radius;
            float sy = y + process.sin(a) * radius;
            process.vertex(sx, sy);
        }
        process.endShape(process.CLOSE);
    }

    public String toString() {
        return String.format("%s, %s, %s", x, y, state.toString());
    }


}
