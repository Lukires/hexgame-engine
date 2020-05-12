package com.lukire.hexgame.main;

import com.lukire.hexgame.board.SlotState;
import com.lukire.hexgame.game.ArtificalPlayer;
import com.lukire.hexgame.game.GameHandler;
import com.lukire.hexgame.game.GameState;

import static org.junit.jupiter.api.Assertions.*;

class FullGameTest {

    @org.junit.jupiter.api.Test
    void zeroDepthGame() {
        assertTrue(assertGame(0));
    }

    @org.junit.jupiter.api.Test
    void depthGame() {
        assertTrue(assertGame(1));
    }

    boolean assertGame(int depth) {
        Main main = new Main();
        main.ai = new ArtificalPlayer(main.game);
        main.game.setState(GameState.AI_ONLY);
        main.ai.setSearchDepth(depth);
        while(main.game.getState()==GameState.AI_ONLY) {
            if (main.game.turn) {
                main.ai.makeMove(SlotState.RED);
            }else{
                main.ai.makeMove(SlotState.BLUE);
            }
        }

        return(main.game.getState()==GameState.FINISHED);
    }
}