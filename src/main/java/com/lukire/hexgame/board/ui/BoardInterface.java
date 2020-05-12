package com.lukire.hexgame.board.ui;

import com.lukire.hexgame.board.GameBoard;
import com.lukire.hexgame.board.SlotState;
import com.lukire.hexgame.exceptions.NoHexagonException;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class BoardInterface {

    private GameBoard gameBoard;

    public static final int radius = 20;
    public static final int radiusSquared = (int) Math.pow(radius, 2);
    private ArrayList<Hexagon> hexagons = new ArrayList<Hexagon>();
    private PApplet process;
    public BoardInterface(GameBoard gameBoard, PApplet process) {
        this.gameBoard=gameBoard;
        this.process=process;
        refresh();
    }

    public void refresh() {
        for (int x = 0; x < gameBoard.getWidth(); x++) {
            for (int y = 0; y < gameBoard.getHeight(); y++) {
                hexagons.add(new Hexagon(x, y, gameBoard.nodes[x][y].getState()));
            }
        }
    }

    public void display() {
        refresh();
        process.pushMatrix();
        for (Hexagon hexagon : hexagons) {
            hexagon.draw(process);
        }
        process.popMatrix();
    }


    //Dette er programmets største bottle neck
    public Hexagon getHexagon(float GameX, float GameY) throws NoHexagonException {

        float mouseX = process.mouseX;
        float mouseY = process.mouseY;

        for (Hexagon hexagon : hexagons) {

            //Vi er kun interesseret i at finde slots der ikke er optaget
            if (hexagon.getState() != SlotState.EMPTY) {
                continue;
            }

            PVector center = hexagon.getCenter();

            //Denne matematik fjerner de fleste elementer fra vores liste, hvilket betyder at vi ikke kører den mere advanceret matematik forneden
            if (center.x-mouseX > radius+5 || center.x-mouseX < -radius+5 || center.y-mouseY > radius+5 || center.y-mouseY < -radius+5) {
                continue;
            }

            //Getting the squareroot takes time, so we just get the square instead
            if (Math.pow(mouseX - center.x, 2) + Math.pow(mouseY - center.y, 2)< radiusSquared-4) {
                return hexagon;
            }
        }

        throw new NoHexagonException(GameX, GameY);

    }
}