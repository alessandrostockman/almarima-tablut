package it.unibo.almarima.tablut.almarima.coordinates;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Coordinates {

    private static Coord[][] allCoordinates;
    private static int size;
    private static boolean isSet = false;
    private static ArrayList<Coord> citadels;

    // Sets all coords, should ONLY BE called once.
    public static void setAllCoordinates(int max) {
        if (!isSet) {
            size = max;
            allCoordinates = new Coord[max][max];
            for (int i = 0; i < max; i++) {
                for (int j = 0; j < max; j++) {
                    allCoordinates[i][j] = new Coord(i, j);
                }
            }
            isSet = true;
            
            citadels= new ArrayList<>();
            createCitadels();
        }
    }

    public static void createCitadels(){
        ArrayList<String> cit= new ArrayList<>();
        cit.add("a4");
		cit.add("a5");
		cit.add("a6");
		cit.add("b5");
		cit.add("d1");
		cit.add("e1");
		cit.add("f1");
		cit.add("e2");
		cit.add("i4");
		cit.add("i5");
		cit.add("i6");
		cit.add("h5");
		cit.add("d9");
		cit.add("e9");
		cit.add("f9");
        cit.add("e8");
        
        for (String c : cit) {
            citadels.add(get(c));
        }
    }

    public static Coord get(String position){
        if (position.length() == 2) {
			int x = Character.toLowerCase(position.charAt(0)) - 97;
            int y = Integer.parseInt(position.charAt(1) + "") - 1;
            return allCoordinates[x][y];

		} else {
			throw new InvalidParameterException("the FROM and the TO string must have length=2");
        }
        
    }

    public static Coord get(int i, int j) {
        return allCoordinates[i][j];
    }

    public static boolean isEscape(Coord c) {
        return isEscape(c.x, c.y);
    }

    public static boolean isEscape(int i, int j) { // Very efficient way to check if something is a corner.
        if (i * j != 0)
            return i == size - 1 || j == size - 1;
        return true;
    }

    public static boolean isCenterOrNeighborCenter(Coord c) {
        return isCenterOrNeighborCenter(c.x, c.y);
    }

    public static boolean isCenterOrNeighborCenter(int x, int y) {
        if (!(x == 4 || y == 4))
            return false;
        return Math.abs(x - y) <= 1;
    }

    public static boolean isCenter(Coord c) {
        return isCenter(c.x, c.y);
    }

    public static boolean isCenter(int x, int y) {
        return x == 4 && y == 4;
    }

    public static List<Coord> getCorners() {
        return Arrays.asList(get(0, 0), get(0, size - 1), get(size - 1, 0), get(size - 1, size - 1));
    }

    public static List<Coord> getNeighbors(Coord c) {
        List<Coord> neighbors = new ArrayList<>();
        for (int incr : Arrays.asList(-1, 1)) {
            try {
                neighbors.add(get(c.x + incr, c.y));
            } catch (IndexOutOfBoundsException e) {
            }
            try {
                neighbors.add(get(c.x, c.y + incr));
            } catch (IndexOutOfBoundsException e) {
            }
        }
        return neighbors;
    }

    // Returns the coordinate with which a sandwich would be made around "middle",
    // using "front".
    public static Coord getSandwichCoord(Coord front, Coord middle) throws CoordinateDoesNotExistException {
        int xDiff = front.x - middle.x;
        int yDiff = front.y - middle.y;
        if (xDiff * yDiff != 0 || xDiff + yDiff == 0) {
            throw new CoordinateDoesNotExistException(
                    "These coordinates are not adjacent: " + front.toString() + " and " + middle.toString());
        }
        try {
            Coord back = get(middle.x - xDiff, middle.y - yDiff);
            return back;
        } catch (IndexOutOfBoundsException e) {
            throw new CoordinateDoesNotExistException("The sandwich coordinate would be off the board.");
        }
    }

    // check if Coord is a Citadel 
    public static boolean isCitadel(Coord c){
        return citadels.contains(c);
    }

    // Given a coordinate, returns the distance between it and the closest corner.
    public static int distanceToClosestCorner(Coord kingPos) {
        List<Coord> corners = getCorners();
        int minDistance = Integer.MAX_VALUE;
        for (Coord corner : corners) {
            int distance = kingPos.distance(corner);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
        return minDistance;
    }

    // Lol at the insanity of OOP, two anonymous classes just to iterate!
    public static Iterable<Coord> iterCoordinates() {
        return new Iterable<Coord>() {
            @Override
            public Iterator<Coord> iterator() {
                return new Iterator<Coord>() {
                    private int i = 0;
                    private int j = 0;

                    @Override
                    public boolean hasNext() {
                        return i != size;
                    }

                    @Override
                    public Coord next() {
                        int iOld = i;
                        int jOld = j;

                        // Now update i and j for the next boy.
                        j += 1;
                        if (j == size) {
                            j = 0;
                            i += 1;
                        }
                        return allCoordinates[iOld][jOld];
                    }

                    @Override
                    public void remove() {
                    }
                };
            }
        };
    }

    
    public static ArrayList<Coord> getCitadels() {
        return citadels;
    }


    // useful exception
    public static class CoordinateDoesNotExistException extends Exception {
        private static final long serialVersionUID = 1L;

        public CoordinateDoesNotExistException(String message) {
            super(message);
        }
    }

}
