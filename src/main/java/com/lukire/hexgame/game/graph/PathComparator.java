package com.lukire.hexgame.game.graph;

import java.util.Comparator;

public class PathComparator implements Comparator<Path> {
    public int compare(Path to, Path from) {
        return to.getLength() < from.getLength()? -1 : to.getLength() == from.getLength() ? 0: 1;
    }
}
