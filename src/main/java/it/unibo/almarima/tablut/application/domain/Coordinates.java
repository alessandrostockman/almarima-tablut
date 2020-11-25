package it.unibo.almarima.tablut.application.domain;

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
    private static ArrayList<Coord> escapes;

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
            
            //initialize citadels positions
            citadels= new ArrayList<>();
            initCitadels();

            //initialize escape tiles positions
            escapes= new ArrayList<>();
            initEscapes();

        }
    }

    public static void initCitadels(){
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
    
    //TODO: aggiungere gli angoli ? non è chiaro se cambi qualcosa o meno 
    public static void initEscapes(){
        escapes.add(get(0,1));
        escapes.add(get(0,2));
        escapes.add(get(0,6));
        escapes.add(get(0,7));
        escapes.add(get(1,0));
        escapes.add(get(1,8));
        escapes.add(get(2,0));
        escapes.add(get(2,8));
        escapes.add(get(6,0));
        escapes.add(get(6,8));
        escapes.add(get(7,0));
        escapes.add(get(7,8));
        escapes.add(get(8,1));
        escapes.add(get(8,2));
        escapes.add(get(8,6));
        escapes.add(get(8,7));    
    }

    public static Coord get(String position){
        if (position.length() == 2) {
            int x = Integer.parseInt(position.charAt(1) + "") - 1;
			int y = Character.toLowerCase(position.charAt(0)) - 97;
            return allCoordinates[x][y];

		} else {
			throw new InvalidParameterException("the FROM and the TO string must have length=2");
        }
        
    }

    public static Coord get(int i, int j) {
        return allCoordinates[i][j];
    }

    public static boolean isEscape(Coord c) {
        return escapes.contains(c);
    }


    //check if the coord passed is on an edge , it's not necessary an escape tile but if i already checked other conditions (ToCoord is legal) can be more efficient
    public static boolean isEscapeAfterConditions(int i, int j) {           
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

    // check if Coord is a Citadel 
    public static boolean isCitadel(Coord c){
        return citadels.contains(c);
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

    
    // Given a coordinate, returns the distance between it and the closest escape tile.
    public static int distanceToClosestEscape(Coord kingPos) {
        List<Coord> escapes = getEscapes();
        int minDistance = Integer.MAX_VALUE;
        for (Coord esc : escapes) {
            int distance = kingPos.distance(esc);
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

    public static ArrayList<Coord> getEscapes() {
        return escapes;
    }

    
    // useful exception
    public static class CoordinateDoesNotExistException extends Exception {
        private static final long serialVersionUID = 1L;

        public CoordinateDoesNotExistException(String message) {
            super(message);
        }
    }

}
