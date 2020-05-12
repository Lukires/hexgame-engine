package com.lukire.hexgame.board.ui;

import processing.core.PApplet;
import processing.core.PConstants;

public class Button {

    private int x, y, height, width, id;
    private String text;
    private boolean hover = false;

    public int getX() {
        return x;
    }

    public boolean isHover() {
        return hover;
    }

    public void setHover(boolean hover) {
        this.hover = hover;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }


    public Button(int x, int y, int width, int height, String text, int id) {
        this.x=x;
        this.y=y;
        this.height=height;
        this.width=width;
        this.text=text;
        this.id=id;
    }

    public void display(PApplet screen) {

        screen.stroke(0,0,0);
        if(hover){
            screen.fill(200,200,200);
        }else{
            screen.fill(255,255,255);
        }

        screen.rect(x,y,width,height);
        screen.textSize(18);
        screen.fill(0,0,0);
        screen.textAlign(PConstants.CENTER, PConstants.CENTER);
        screen.text(text,x,y, width, height);

    }
}
