package it.unibo.almarima.tablut.application.domain;

import java.util.ArrayList;
import java.util.List;

public final class Coord {
    public final int x; // make it final so they cannot be changed
    public final int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Computes manhattan distance between this coord and the other.
    public int distance(Coord c) {
        return Math.abs(this.x - c.x) + Math.abs(this.y - c.y);
    }

    /**
     * Returns a list of all the coordinates between this coord and another
     * coordinate. Note that it assumes they are either in the same row or same
     * column, otherwise it will return an empty list.
     */
    public List<Coord> getCoordsBetween(Coord c) {
        List<Coord> coords = new ArrayList<Coord>();
        boolean updateY = this.x == c.x; // update y if x's are the same
        boolean updateX = this.y == c.y; // update x if y's are the same

        // So tedious, but necessary, I guess...
        int start, end;
        if (updateX && !updateY) {
            start = this.x;
            end = c.x;
        } else if (updateY && !updateX) {
            start = this.y;
            end = c.y;
        } else {
            return coords;
        }

        // Set the increment and then do the incrementation.
        int incr = (start > end) ? -1 : 1;
        int i = start;
        while (i != end) {
            i += incr;
            if (updateX)
                coords.add(Coordinates.get(i, this.y));
            else if (updateY)
                coords.add(Coordinates.get(this.x, i));
        }
        return coords;
    }

    /**
     * Returns the maximum coordinate difference between two coords.
     */
    public int maxDifference(Coord c) {
        return Math.max(Math.abs(this.x - c.x), Math.abs(this.y - c.y));
    }

    @Override
    public String toString() {
        return String.format("(%d %d)", this.x, this.y);
    }

}
