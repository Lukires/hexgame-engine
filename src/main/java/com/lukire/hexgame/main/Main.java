package com.lukire.hexgame.main;

import com.lukire.hexgame.board.SlotState;
import com.lukire.hexgame.board.ui.Button;
import com.lukire.hexgame.game.ArtificalPlayer;
import com.lukire.hexgame.game.GameHandler;
import com.lukire.hexgame.game.GameState;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.ArrayList;

public class Main extends PApplet {


    public GameHandler game = new GameHandler(this);
    public ArrayList<Button> buttons = new ArrayList<Button>();
    public ArtificalPlayer ai;

    public static void main(String[] args) {
        main("com.lukire.hexgame.main.Main");
    }

    public void settings() {
        size(580,350);
    }

    public void setup() {
        ai = new ArtificalPlayer(game);
        buttons.add(new Button(width/4,height/4-25,width-(width/4)*2,100,"Spil med ven",1));
        buttons.add(new Button(width/4,height/2+25,width-(width/4)*2,100,"Spil med computer",2));
        frameRate(30);
    }

    public void draw() {

        clear();
        background(255,255,255);
        GameState state = game.getState();
        switch(state) {
            case TWO_PLAYER:
                game.refresh();
                textSize(20);
                if (game.turn) {
                    fill(255,0,0);
                    text("Røds tur", 75,height-50);
                }else {
                    fill(0, 0, 255);
                    text("Blås tur", 75,height-50);
                }
                break;
            case ONE_PLAYER:
                game.refresh();

                fill(0,0,0);
                textSize(20);
                text("Depth: "+ai.getSearchDepth(),width-75,25);
                textSize(12);
                text("Vælg et tal på dit tastatur for at ændre AI depth", width-120, 30,100,100);

                textSize(20);
                if (game.turn) {
                    ai.makeMove(SlotState.RED);
                    fill(255,0,0);
                    text("Røds tur", 75,height-50);
                }else{
                    fill(0, 0, 255);
                    text("Blås tur", 75,height-50);
                }
                break;
            case AI_ONLY:
                game.refresh();
                if (game.turn) {
                    ai.makeMove(SlotState.RED);
                }else{
                    ai.makeMove(SlotState.BLUE);
                }
                break;
            case SELECTION:
                clear();
                background(255, 255, 255);
                for(Button button : buttons) {
                    button.display(this);
                }
                break;
            case FINISHED:
                game.refresh();
                clear();
                background(255, 255, 255);
                textSize(48);
                fill(0,0,0);
                textAlign(CENTER, CENTER);
                if (game.turn) {
                    text("Blå vandt!", width/2, height/2);
                }else{
                    text("Rød vandt!", width/2, height/2);
                }
                textSize(24);
                text("Klik for at prøve igen!", width/2, height/2 + 50);
                break;
        }

    }

    public void keyPressed(KeyEvent e) {
        char key = e.getKey();
        try{
            ai.setSearchDepth(Integer.parseInt(key+""));
        }catch(NumberFormatException ex) {
            //Gør ikke noget fordi vi er ligeglade med dette
        }
    }
    public void mouseMoved(MouseEvent e) {
        if (game.getState()== GameState.SELECTION) {
            for(Button button : buttons) {
                button.setHover(mouseX >= button.getX() && mouseX <= button.getX()+button.getWidth() && mouseY >= button.getY() && mouseY <= button.getY()+button.getHeight());
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        if(game.getState() == GameState.FINISHED) {
            game = new GameHandler(this);
            ai = new ArtificalPlayer(game);
            game.setState(GameState.SELECTION);
            return;
        }

        if (game.getState()==GameState.SELECTION) {
            for(Button button : buttons) {
                if (mouseX >= button.getX() && mouseX <= button.getX()+button.getWidth() && mouseY >= button.getY() && mouseY <= button.getY()+button.getHeight()) {
                    if (button.getId()==1) {
                        game.setState(GameState.TWO_PLAYER);
                    }else{
                        game.setState(GameState.ONE_PLAYER);
                    }
                }
            }
            return;
        }


        //Vi gør så at spilleren ikke kan placere brikker for vores AI
        if (game.getState()==GameState.ONE_PLAYER) {
            if (game.turn) {
                return;
            }
        }
        game.click(e.getX(), e.getY());
    }

}
