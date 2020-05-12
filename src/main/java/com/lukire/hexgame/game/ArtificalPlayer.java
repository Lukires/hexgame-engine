package com.lukire.hexgame.game;

import com.lukire.hexgame.board.GameBoard;
import com.lukire.hexgame.board.SlotState;
import com.lukire.hexgame.exceptions.IllegalMoveException;
import com.lukire.hexgame.game.graph.Graph;
import com.lukire.hexgame.game.graph.Node;
import com.lukire.hexgame.game.graph.Path;
import com.lukire.hexgame.game.graph.PathComparator;
import com.lukire.hexgame.main.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class ArtificalPlayer {

    private int searchDepth = 3;
    private GameHandler handler;

    public int getSearchDepth() {
        return searchDepth;
    }

    public void setSearchDepth(int searchDepth) {
        this.searchDepth = searchDepth;
    }

    public ArtificalPlayer(GameHandler handler) {
        this.handler=handler;
    }

    private PriorityQueue<Path> paths = new PriorityQueue<Path>();

    private int getHeuristicScore(SlotState turn, Graph graph) {
        int us = evaluateState(turn, graph);
        int other = evaluateState(turn==SlotState.BLUE?SlotState.RED:SlotState.BLUE, graph);

        if(us==Integer.MIN_VALUE) {
            return us;
        }

        if(other==Integer.MIN_VALUE) {
            return Integer.MAX_VALUE;
        }

        return us-other;
    }

    private int evaluateState(SlotState turn, Graph graph) {

        visited=new int[11][11];
        for(int[] row : visited) {
            Arrays.fill(row, -1);
        }
        PriorityQueue<Path> paths = new PriorityQueue<Path>(new PathComparator());
        if (turn == SlotState.RED) {
            for(int y = 0; y < 11; y++) {
                for(Path path : graph.getPaths(graph.nodes[0][y])) {
                    paths.add(path);
                }
            }
        }else{
            for(int x = 0; x < 11; x++) {
                for(Path path : graph.getPaths(graph.nodes[x][0])) {
                    paths.add(path);
                }
            }
        }

        Path path = shortestPath(paths, turn, graph);
        if (path==null) {
            return Integer.MAX_VALUE;
        }
        return path.getLength()<=1?Integer.MIN_VALUE:path.getLength();
    }

    private int[][] visited;

    //Djikstra shortest path finding
    private Path shortestPath(PriorityQueue<Path> paths, SlotState state, Graph graph) {

        Path path = paths.poll();

        if (path==null) {
            return null;
        }
        if ((state == SlotState.RED && path.getTo().getX()==10) || (state == SlotState.BLUE && path.getTo().getY()==10)) {
            if(path.getFrom().getState()==SlotState.EMPTY) {
                path.setLength(path.getLength()+1);
            }
            return path;
        }
        for (Node nodes : graph.getConnections(path.getTo())){
            if ((state == SlotState.RED && nodes.getX()==0) || (state == SlotState.BLUE && nodes.getY()==0)){
                continue;
            }
            if(nodes.getState()!=SlotState.EMPTY && nodes.getState() != state) {
                continue;
            }
            if (visited[nodes.getX()][nodes.getY()]!=-1) {
                if(path.getLength()>=visited[nodes.getX()][nodes.getY()]) {
                    continue;
                }
            }else {
                visited[nodes.getX()][nodes.getY()] = path.getLength();
            }

            int length = path.getLength()+((nodes.getState()!=state&&nodes.getState()!=SlotState.EMPTY)?Integer.MAX_VALUE:(nodes.getState()==SlotState.EMPTY?1:0));

            Node from = path.getFrom().getState()==SlotState.EMPTY?path.getFrom():path.getTo();
            paths.add(new Path(from, nodes, length));

        }

        return shortestPath(paths, state, graph);

    }

    //Alpha beta proning - Gå efter max alpha og mindt beta

    public int evaluateMove(SlotState turn, SlotState enemy, Graph alphaGraph, int depth, int alpha, int beta) {
        if (depth <= 0 /*|| artificialState has no empty spots left*/) {
            return getHeuristicScore(turn, alphaGraph);
        }

        //The next turn (The turn after turn)
        SlotState newTurn = turn==SlotState.BLUE?SlotState.RED:SlotState.BLUE;

        boolean max = turn!=enemy;

        int best = max?Integer.MIN_VALUE:Integer.MAX_VALUE;
        for (int x = 0; x < 11; x++) {
            for (int y = 0; y < 11; y++) {
                Graph graph = Graph.cloneGraph(alphaGraph);
                SlotState state = graph.nodes[x][y].getState();
                if (state!=SlotState.EMPTY) {
                    continue;
                }

                graph.makeMove(x, y, turn);
                int temp = evaluateMove(newTurn, enemy, graph, (--depth), alpha, beta);
                graph.nodes[x][y].setState(SlotState.EMPTY);
                if (graph.start == null) {
                    graph.start = graph.nodes[x][y];
                }
                if(max)
                {
                    best = Math.max(best, temp);
                    alpha = Math.max(alpha, best);
                }else{
                    best = Math.min(best, temp);
                    beta = Math.min(beta, best);
                }

                if (beta <= alpha) {
                    break;
                }
            }
        }
        return best;
    }

    public Node bestNode(SlotState turn) {

        GameBoard board = handler.getBoard();
        SlotState other = turn==SlotState.BLUE?SlotState.RED:SlotState.BLUE;

        Node bestNode = null;
        int bestScore = Integer.MAX_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        for (int x = 0; x < 11; x++) {
            for (int y = 0; y < 11; y++) {

                Graph graph = Graph.cloneGraph(board);
                Node node = graph.nodes[x][y];

                SlotState state = node.getState();
                if (state != SlotState.EMPTY) {
                    continue;
                }

                Node[][] tempNode = graph.nodes;;
                tempNode[x][y].setState(turn);

                int score = evaluateMove(other, other, graph, searchDepth,alpha,beta);
                if (score <= bestScore) {
                    bestScore = score;
                    bestNode=node;
                }
                alpha = Math.min(alpha, bestScore);
            }

        }
        return bestNode;
    }

    public void makeMove(SlotState turn) {


        try{
            Node node = bestNode(turn);
            handler.newMove(node.getX(), node.getY());
        }catch(IllegalMoveException e) {
            System.out.println(e.toString());
        }


    }



}
