package com.lukire.hexgame.game.graph;

import com.lukire.hexgame.board.SlotState;
import com.lukire.hexgame.game.Move;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph {

    public Node[][] nodes;

    protected int height;
    protected int width;

    public Node start = null;
    protected ArrayList<Move> moves = new ArrayList<Move>();

    public Graph(int width, int height) {
        this.height=height;
        this.width=width;
        this.nodes=new Node[height][width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.nodes[x][y] = new Node(x, y, SlotState.EMPTY);
            }
        }
    }


    //Connections are based purely on coordinates
    public ArrayList<Node> getConnections(Node node) {

        int x = node.getX();
        int y = node.getY();

        ArrayList<Node> neighbors = new ArrayList<Node>();

        if(y>0){
            neighbors.add(nodes[x] [y-1]);
            if (x < 10) {
                neighbors.add(nodes[x+1][y-1]);
            }
        }

        if (y<10) {
            neighbors.add(nodes[x][y+1]);
            if (x>0) {
                neighbors.add(nodes[x-1][y+1]);
            }
        }

        if(x>0) {
            neighbors.add(nodes[x-1][y]);
        }

        if (x<10) {
            neighbors.add(nodes[x+1][y]);
        }

        return neighbors;
    }

    public ArrayList<Path> getPaths(Node node) {
        ArrayList<Path> paths = new ArrayList<Path>();
        HashMap<Node, Node> fromTo = new HashMap<Node,Node>();

        for(Node to : getConnections(node)) {
            if (to == node) {
                continue;
            }

            if(to.getX() == node.getX() && to.getY() == node.getY()) {
                continue;
            }

            if (node.getState() != to.getState() && to.getState()!=SlotState.EMPTY) {
                continue;
            }
            if (fromTo.containsValue(node) && fromTo.containsKey(to)) {
                continue;
            }

            paths.add(new Path(node, to, to.getState()==SlotState.EMPTY?1:0));
            fromTo.put(node,to);
        }
        return paths;
    }

    public void makeMove(int x, int y, SlotState state) {
        this.nodes[x][y].setState(state);
        this.moves.add(new Move(x, y, state));
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }



    public static Graph cloneGraph(Graph graph) {
        Graph clone = new Graph(graph.height,graph.width);
        for(int x = 0; x<graph.width; x++) {
            for(int y = 0; y<graph.height; y++) {
                clone.nodes[x][y].setState(graph.nodes[x][y].getState());
            }
        }
        return clone;
    }

}
