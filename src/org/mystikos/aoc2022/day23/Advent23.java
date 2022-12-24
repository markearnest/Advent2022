package org.mystikos.aoc2022.day23;

import java.io.BufferedReader;
import java.util.*;

record Point(int x, int y) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }
}
class Elf {
    public static final int EAST = 3;
    public static final int SOUTH = 1;
    public static final int WEST = 2;
    public static final int NORTH = 0;
    final int serialNumber;
    private Point proposedMove;
    public Elf(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    private boolean anyoneNearBy(Point me, Map<Elf, Point> elves) {
        if(elves.containsValue(new Point(me.x(), me.y() + 1))) {
            return true;
        }
        if(elves.containsValue(new Point(me.x(), me.y() - 1))) {
            return true;
        }
        if(elves.containsValue(new Point(me.x() + 1, me.y()))) {
            return true;
        }
        if(elves.containsValue(new Point(me.x() - 1, me.y()))) {
            return true;
        }
        if(elves.containsValue(new Point(me.x() + 1, me.y() + 1))) {
            return true;
        }
        if(elves.containsValue(new Point(me.x() + 1, me.y() - 1))) {
            return true;
        }
        if(elves.containsValue(new Point(me.x() - 1, me.y() + 1))) {
            return true;
        }
        return elves.containsValue(new Point(me.x() - 1, me.y() - 1));
    }
    public Point proposeMove(Point me, HashMap<Elf, Point> elves, int direction) {
        this.proposedMove = null;
        if(!anyoneNearBy(me, elves)) {
            return null;
        }
        for(int i = 0; i < 4 ; i++) {
            int dir = (direction + i) % 4;
            switch (dir) {
                case NORTH -> {
                    if (!elves.containsValue(new Point(me.x(), me.y() - 1)) && !elves.containsValue(new Point(me.x() - 1, me.y() - 1))
                            && !elves.containsValue(new Point(me.x() + 1, me.y() - 1))) {
                        proposedMove = new Point(me.x(), me.y() - 1);
                        return proposedMove;
                    }
                }
                case SOUTH -> {
                    if (!elves.containsValue(new Point(me.x(), me.y() + 1)) && !elves.containsValue(new Point(me.x() - 1, me.y() + 1))
                            && !elves.containsValue(new Point(me.x() + 1, me.y() + 1))) {
                        proposedMove = new Point(me.x(), me.y() + 1);
                        return proposedMove;
                    }
                }
                case EAST -> {
                    if (!elves.containsValue(new Point(me.x() + 1, me.y())) && !elves.containsValue(new Point(me.x() + 1, me.y() - 1))
                            && !elves.containsValue(new Point(me.x() + 1, me.y() + 1))) {
                        proposedMove = new Point(me.x() + 1, me.y());
                        return proposedMove;
                    }
                }
                case WEST -> {
                    if (!elves.containsValue(new Point(me.x() - 1, me.y())) && !elves.containsValue(new Point(me.x() - 1, me.y() - 1))
                            && !elves.containsValue(new Point(me.x() - 1, me.y() + 1))) {
                        proposedMove = new Point(me.x() - 1, me.y());
                        return proposedMove;
                    }
                }
            }
        }
        return null;
    }
    public Point move(ArrayList<Point> moves) {
        if(proposedMove == null) {
            return null;
        }
        if (Collections.frequency(moves, proposedMove) > 1) {
            return null;
        }
        return proposedMove;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Elf elf = (Elf) o;
        return serialNumber == elf.serialNumber;
    }
    @Override
    public int hashCode() {
        return Objects.hash(serialNumber);
    }
}
public class Advent23 {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String fileName = "input/input23.txt";
        int phase1Answer;
        int phase2Answer = 0;
        ArrayList<Point> proposedMoves = new ArrayList<>();
        HashMap<Elf, Point> elves = new HashMap<>();
        BufferedReader reader;
        int startingX = 0;
        int startingY = 0;
        try {
            reader = new BufferedReader(new java.io.FileReader(fileName));
            String line = reader.readLine();
            int serialNumber = 0;
            while (line != null) {
                for(char c : line.toCharArray()) {
                    if(c == '#') {
                        elves.put(new Elf(serialNumber++), new Point(startingX, startingY));
                    }
                    startingX++;
                }
                startingX = 0;
                startingY++;
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int rounds = 10;
        int direction = Elf.NORTH;
        for(int i = 0; i < rounds; i++) {
            for (Map.Entry<Elf, Point> elf : elves.entrySet()) {
                Point proposedMove = elf.getKey().proposeMove(elf.getValue(), elves, direction);
                if (proposedMove != null) {
                    proposedMoves.add(proposedMove);
                }
            }
            for (Map.Entry<Elf, Point> elf : elves.entrySet()) {
                Point move = elf.getKey().move(proposedMoves);
                if (move != null) {
                    elves.put(elf.getKey(), move);
                }
            }
            direction = (direction + 1) % 4;
            proposedMoves.clear();
            phase2Answer++;
        }
        phase1Answer = processGrid(elves, false);
        int moves = 1;
        while(moves != 0) {
            moves = 0;
            for(Map.Entry<Elf, Point> elf : elves.entrySet()) {
                Point proposedMove = elf.getKey().proposeMove(elf.getValue(), elves, direction);
                if(proposedMove != null) {
                    proposedMoves.add(proposedMove);
                }
            }
            for(Map.Entry<Elf, Point> elf : elves.entrySet()) {
                Point move = elf.getKey().move(proposedMoves);
                if(move != null) {
                    moves++;
                    elves.put(elf.getKey(), move);
                }
            }
            direction = (direction + 1) % 4;
            proposedMoves.clear();
            phase2Answer++;
            System.out.print("Round " + phase2Answer+"\r");
        }
        System.out.println();
        System.out.println("Phase 1 Answer: " + phase1Answer);
        System.out.println("Phase 2 Answer: " + phase2Answer);
        long currentTime = System.currentTimeMillis();
        double elapsedTime = (currentTime - startTime) / 1000.0;
        System.out.println("Time in seconds : " + elapsedTime);
    }
    private static int processGrid(HashMap<Elf, Point> elves, @SuppressWarnings("SameParameterValue") boolean print) {
        int maxX = 0;
        int maxY = 0;
        int minX = 0;
        int minY = 0;
        for(Map.Entry<Elf, Point> e : elves.entrySet()) {
            if(e.getValue().x() > maxX) {
                maxX = e.getValue().x();
            }
            if(e.getValue().x() < minX) {
                minX = e.getValue().x();
            }
            if(e.getValue().y() > maxY) {
                maxY = e.getValue().y();
            }
            if(e.getValue().y() < minY) {
                minY = e.getValue().y();
            }
        }
        int totalPoints = 0;
        for(int y = minY; y <= maxY; y++) {
            for(int x = minX; x <= maxX; x++) {
                if(elves.containsValue(new Point(x, y))) {
                    if (print) System.out.print("#");
                } else {
                    totalPoints++;
                    if (print) System.out.print(".");
                }
            }
            if (print) System.out.println();
        }
        return totalPoints ;
    }
}
