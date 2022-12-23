package org.mystikos.aoc2022.day22;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Objects;

class Player {
    public static final int EAST = 0;
    public static final int SOUTH = 1;
    public static final int WEST = 2;
    public static final int NORTH = 3;
    private int x;
    private int y;
    private boolean phase2;
    public boolean isPhase2() {
        return phase2;
    }
    public void setPhase2(boolean phase2) {
        this.phase2 = phase2;
    }
    public int getDirection() {
        return direction;
    }
    private int direction;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.direction = EAST;
        phase2 = false;
    }
    public void turn(Character turnDirection) {
        if(turnDirection == 'R') {
            direction++;
        } else {
            direction--;
        }
        if(direction > NORTH) {
            direction = EAST;
        } else if(direction < EAST) {
            direction = NORTH;
        }
    }
    public void move(int distance, char[][] map) {
        for(int i = 0; i < distance; i++) {
            int[] nextPosition = getNextPosition();
            if(isValidCoordinate(nextPosition[0], nextPosition[1], map)) {
                if(isSpace(nextPosition[0], nextPosition[1], map)) {
                    nextPosition = flyThroughSpace(nextPosition[0], nextPosition[1], map);
                    if(nextPosition[0] < 0 && nextPosition[1] < 0) {
                        break;
                    } else {
                        x = nextPosition[0];
                        y = nextPosition[1];
                    }
                } else if(isWall(nextPosition[0], nextPosition[1], map)) {
                    break;
                } else {
                    x = nextPosition[0];
                    y = nextPosition[1];
                }
            } else {
                nextPosition = wrapAround(nextPosition[0], nextPosition[1], map);
                if(nextPosition[0] < 0 && nextPosition[1] < 0) {
                    break;
                } else {
                    x = nextPosition[0];
                    y = nextPosition[1];
                }
            }
        }
    }
    public boolean isSpace(int x, int y, char[][] map) {
        return map[y][x] == ' ' || map[y][x] == '\u0000';
    }
    public int[] flyThroughSpace(int x, int y, char[][] map) {
        int[] nextPosition = getNextPosition(x, y);
        if(isValidCoordinate(nextPosition[0], nextPosition[1], map)) {
            if(isSpace(nextPosition[0], nextPosition[1], map)) {
                return flyThroughSpace(nextPosition[0], nextPosition[1], map);
            } else if(isWall(nextPosition[0], nextPosition[1], map)) {
                return new int[]{-1, -1}; // hit a wall, go back
            } else {
                return nextPosition;
            }
        } else {
            return wrapAround(nextPosition[0], nextPosition[1], map);
        }
    }

    private int[] wrapAround(int x, int y, char[][] map) {
        if(this.isPhase2()) {
            return wrapAroundPhase2(x, y, map);
        }
        return wrapAroundPhase1(x, y, map);
    }
    public int[] getCoodinates() {
        return new int[] {x, y};
    }
    private int[] wrapAroundPhase1(int x, int y, char[][] map) {
        int tempx = x;
        int tempy = y;
        switch (direction) {
            case EAST -> tempx = 1;
            case SOUTH -> tempy = 1;
            case WEST -> tempx = map[y].length - 1;
            case NORTH -> tempy = map.length - 1;
        }
        while(map[tempy][tempx] == ' ') {
            int[] nextPosition = getNextPosition(tempx, tempy);
            tempx = nextPosition[0];
            tempy = nextPosition[1];
        }
        if(isWall(tempx,tempy, map)) {
            return new int[] {-1, -1}; // hit a wall, go back
        } else {
            return new int[] {tempx, tempy};
        }
    }
    private int[] wrapAroundPhase2(int x, int y, char[][] map) {
        if(x == 102 && y==4) {
            System.out.println("STOP");
        }
        int tempx = x <= 0 ? 1 : x >= map[0].length ? map[0].length - 1 : x;
        int tempy = y <= 0 ? 1 : y >= map.length ? map.length - 1 : y;
        int newDirection = direction;
        int mod = (map.length - 1) / 4;
        switch (this.getDirection()) {
            case EAST -> {
                switch (whichFraction(tempy - 1, map.length - 1, 4) + 1) {
                    case 1 -> {
                        tempy = fractionTranslation(tempy % mod - 1, map.length - 1, 3, 4, true);
                        tempx = map[tempy].length - 1;
                        newDirection = WEST;
                    }
                    case 2 -> {
                        tempx = fractionTranslation(tempy % mod, map[tempy].length - 1, 3, 3, false);
                        tempy = map.length - 1;
                        newDirection = NORTH;
                    }
                    case 3 -> {
                        tempy = fractionTranslation(tempy % mod - 1, map.length - 1, 1, 4, true);
                        tempx = map[tempy].length - 1;
                        newDirection = WEST;
                    }
                    case 4 -> {
                        tempx = fractionTranslation(tempy % mod, map[tempy].length - 1, 2, 3, false);
                        tempy = map.length - 1;
                        newDirection = NORTH;
                    }
                }
            }
            case SOUTH -> {
                switch (whichFraction(tempx - 1, map[tempy].length - 1, 3) + 1) {
                    case 1 -> {
                        tempx = fractionTranslation(tempx % mod, map[tempy].length - 1, tempx == mod ? 4 : 3, 3, false);
                        tempy = 1;
                        newDirection = SOUTH;
                    }
                    case 2 -> {
                        tempy = fractionTranslation(tempx % mod, map.length - 1, 4, 4, false);
                        tempx = map[tempy].length - 1;
                        newDirection = WEST;
                    }
                    case 3 -> {
                        tempy = fractionTranslation(tempx % mod, map.length - 1, 2, 4, false);
                        tempx = map[tempy].length - 1;
                        newDirection = WEST;
                    }
                }
            }
            case WEST -> {
                switch (whichFraction(tempy - 1, map.length - 1, 4) + 1) {
                    case 1 -> {
                        tempy = fractionTranslation(tempy % mod - 1, map.length - 1, tempy == mod ? 2 : 3, 4, true);
                        tempx = 1;
                        newDirection = EAST;
                    }
                    case 2 -> {
                        tempx = fractionTranslation(tempy % mod, map[tempy].length - 1, 1, 3, false);
                        tempy = 1;
                        newDirection = SOUTH;
                    }
                    case 3 -> {
                        tempy = fractionTranslation(tempy % mod - 1, map.length, 1, 4, true);
                        tempx = 1;
                        newDirection = EAST;
                    }
                    case 4 -> {
                        tempx = fractionTranslation(tempy % mod, map[tempx].length - 1, tempy == 200 ? 3 : 2, 3, false);
                        tempy = 1;
                        newDirection = SOUTH;
                    }
                }
            }
            case NORTH -> {
                switch (whichFraction(tempx - 1, map[tempy].length - 1, 3) + 1) {
                    case 1 -> {
                        tempy = fractionTranslation(tempx % mod, map.length - 1, tempx == mod ? 3 : 2, 4, false);
                        tempx = 1;
                        newDirection = EAST;
                    }
                    case 2 -> {
                        tempy = fractionTranslation(tempx % mod, map.length - 1, 4, 4, false);
                        tempx = 1;
                        newDirection = EAST;
                    }
                    case 3 -> {
                        tempx = fractionTranslation(tempx % mod, map[tempy].length - 1, tempx == 150 ? 2 : 1, 3, false);
                        tempy = map.length - 1;
                        newDirection = NORTH;
                    }
                }
            }
        }
        while(map[tempy][tempx] == ' ') {
            int[] nextPosition = getNextPosition(tempx, tempy, newDirection);
            tempx = nextPosition[0];
            tempy = nextPosition[1];
        }
        if(isWall(tempx, tempy, map)) {
            return new int[] {-1, -1}; // hit a wall, go back
        } else {
            this.direction = newDirection;
            return new int[] {tempx, tempy};
        }
    }
    public int whichFraction(int i, int size, int denominator) {
        if(denominator == 3 && i == size) {
            return 2;
        }
        if(denominator == 4 && i == size) {
            return 3;
        }
        return (int) Math.floor((double) i / (double) size * denominator);
    }
    public int fractionTranslation(int i, int size, int numerator, int denominator, boolean inverted) {
        if(inverted) {
            return size / denominator * (numerator) - i;
        }
        return size / denominator * (numerator - 1) + i;
    }
    private boolean isValidCoordinate(int x, int y, char[][] map) {
        return x > 0 && y > 0 && y < map.length && x < map[y].length;
    }
    private boolean isWall(int x, int y, char[][] map) {
        return map[y][x] == '#';
    }
    private int[] getNextPosition() {
        return getNextPosition(this.x, this.y);
    }
    private int[] getNextPosition(int x, int y) {
        return switch (direction) {
            case NORTH -> new int[]{x, y - 1};
            case EAST -> new int[]{x + 1, y};
            case SOUTH -> new int[]{x, y + 1};
            case WEST -> new int[]{x - 1, y};
            default -> null;
        };
    }
    private int[] getNextPosition(int x, int y, int dir) {
        return switch (dir) {
            case NORTH -> new int[]{x, y - 1};
            case EAST -> new int[]{x + 1, y};
            case SOUTH -> new int[]{x, y + 1};
            case WEST -> new int[]{x - 1, y};
            default -> null;
        };
    }
}
public class Advent22 {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String fileName = "input/input22.txt";
        int Phase1Answer;
        int Phase2Answer;
        BufferedReader reader;
        char[][] grid = new char[0][];
        Object[] instructions = null;
        try {
            reader = new BufferedReader(new java.io.FileReader(fileName));
            String line = reader.readLine();
            ArrayList<ArrayList<Character>> gridList = new ArrayList<>();
            ArrayList<Character> preRow = new ArrayList<>();
            preRow.add(' ');
            gridList.add(preRow); // starting at 1,1 makes it easier on me later
            while (!line.isEmpty()) {
                ArrayList<Character> row = new ArrayList<>();
                row.add(' ');
                for (char c : line.toCharArray()) {
                    row.add(c);
                }
                gridList.add(row);
                line = reader.readLine();
            }
            int maxCol = 0;
            for (ArrayList<Character> row : gridList) {
                if (row.size() > maxCol) {
                    maxCol = row.size();
                }
            }
            grid = new char[gridList.size()][maxCol];
            for (int i = 1; i < gridList.size(); i++) {
                for (int j = 1; j < maxCol; j++) {
                    grid[i][j] = j < gridList.get(i).size() ? gridList.get(i).get(j) : ' ';
                }
            }
            instructions = parseInstructions(reader.readLine());
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int startingX = 1;
        while(grid[1][startingX] != '.') {
            startingX++;
        }
        Player player = new Player(startingX, 1);
        for(Object instruction : Objects.requireNonNull(instructions)) {
            if(instruction instanceof Integer) {
                player.move((Integer) instruction, grid);
            } else {
                player.turn(((String) instruction).charAt(0));
            }
        }
        int[] coordinates = player.getCoodinates();
        Phase1Answer = (4*(coordinates[0])) + (1000*(coordinates[1])) + player.getDirection();
        System.out.println("Phase 1 Answer: " + Phase1Answer);
        player = new Player(startingX, 1);
        player.setPhase2(true);
        for(Object instruction : instructions) {
            if(instruction instanceof Integer) {
                player.move((Integer) instruction, grid);
            } else {
                player.turn(((String) instruction).charAt(0));
            }
        }
        coordinates = player.getCoodinates();
        Phase2Answer = (4*(coordinates[0])) + (1000*(coordinates[1])) + player.getDirection();
        System.out.println("Phase 2 Answer: " + Phase2Answer);
        long currentTime = System.currentTimeMillis();
        double elapsedTime = (currentTime - startTime) / 1000.0;
        System.out.println("Time in seconds : " + elapsedTime);
    }
    private static Object[] parseInstructions(String directions) {
        ArrayList<Object> directionsArray = new ArrayList<>();
        int start = 0;
        int end = 0;
        while (start < directions.length()) {
            if (directions.charAt(start) != 'L' && directions.charAt(start) != 'R') {
                while(end < directions.length() && directions.charAt(end) != 'L' && directions.charAt(end) != 'R') {
                    end++;
                }
                directionsArray.add(Integer.parseInt(directions.substring(start, end)));
            } else if (directions.charAt(start) == 'L' || directions.charAt(start) == 'R') {
                directionsArray.add(directions.substring(start, ++end));
            }
            start = end;
        }
        return directionsArray.toArray();
    }
}
