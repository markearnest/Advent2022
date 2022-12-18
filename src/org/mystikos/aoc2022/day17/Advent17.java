package org.mystikos.aoc2022.day17;

import me.tongfei.progressbar.ProgressBar;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class TetrisColors {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
}

class TetrisResults {
    private final long height;
    private final long pieces;
    public TetrisResults(long height, long pieces) {
        this.height = height;
        this.pieces = pieces;
    }
    public TetrisResults(long height) {
        this.height = height;
        this.pieces = 0;
    }
    public long getHeight() {
        return height;
    }
    public long getPieces() {
        return pieces;
    }
}
class Point {
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    private int x;
    private int y;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
class Piece {
    private final Point[] points;
    public Point[] getPoints() {
        return points;
    }
    public boolean isAtRest() {
        return atRest;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String color;
    private boolean atRest;
    public Piece(Point[] points, String color) {
        this.points = points;
        this.atRest = false;
        this.color = color;
    }
    public Piece(int topLine, int shape) {
        this(new Point(2, topLine + 4), shape);
    }
    public Piece(Point lowerLeft, int shape) {
        points = new Point[(shape==1||shape==2)?5:4];
        atRest = false;
        switch (shape) {
            case 0 -> { // line
                points[0] = lowerLeft;
                points[1] = new Point(lowerLeft.getX() + 1, lowerLeft.getY());
                points[2] = new Point(lowerLeft.getX() + 2, lowerLeft.getY());
                points[3] = new Point(lowerLeft.getX() + 3, lowerLeft.getY());
                color = TetrisColors.ANSI_CYAN;
            }
            case 1 -> { // plus
                points[0] = new Point(lowerLeft.getX() + 1, lowerLeft.getY());
                points[1] = new Point(lowerLeft.getX(), lowerLeft.getY() + 1);
                points[2] = new Point(lowerLeft.getX() + 1, lowerLeft.getY() + 1);
                points[3] = new Point(lowerLeft.getX() + 2, lowerLeft.getY() + 1);
                points[4] = new Point(lowerLeft.getX() + 1, lowerLeft.getY() + 2);
                color = TetrisColors.ANSI_GREEN;
            }
            case 2 -> { // flipped L
                points[0] = lowerLeft;
                points[1] = new Point(lowerLeft.getX() + 1, lowerLeft.getY());
                points[2] = new Point(lowerLeft.getX() + 2, lowerLeft.getY());
                points[3] = new Point(lowerLeft.getX() + 2, lowerLeft.getY() + 1);
                points[4] = new Point(lowerLeft.getX() + 2, lowerLeft.getY() + 2);
                color = TetrisColors.ANSI_YELLOW;
            }
            case 3 -> { // |
                points[0] = lowerLeft;
                points[1] = new Point(lowerLeft.getX(), lowerLeft.getY() + 1);
                points[2] = new Point(lowerLeft.getX(), lowerLeft.getY() + 2);
                points[3] = new Point(lowerLeft.getX(), lowerLeft.getY() + 3);
                color = TetrisColors.ANSI_RED;
            }
            case 4 -> { // square
                points[0] = lowerLeft;
                points[1] = new Point(lowerLeft.getX() + 1, lowerLeft.getY());
                points[2] = new Point(lowerLeft.getX(), lowerLeft.getY() + 1);
                points[3] = new Point(lowerLeft.getX() + 1, lowerLeft.getY() + 1);
                color = TetrisColors.ANSI_PURPLE;
            }
        }
    }
    public int getMaximumX() {
        int max = Integer.MIN_VALUE;
        for (Point point : points) {
            if (point.getX() > max) {
                max = point.getX();
            }
        }
        return max;
    }
    public int getMaximumY() {
        int max = Integer.MIN_VALUE;
        for (Point point : points) {
            if (point.getY() > max) {
                max = point.getY();
            }
        }
        return max;
    }
    public int getMinimumX() {
        int min = Integer.MAX_VALUE;
        for (Point point : points) {
            if (point.getX() < min) {
                min = point.getX();
            }
        }
        return min;
    }
    public void moveDown(ArrayList<Piece> pieces) {
        for (Point point : this.getPoints()) {
            point.setY(point.getY() - 1);
        }
        if(collisionDetection(pieces)) {
            for (Point point : this.getPoints()) {
                point.setY(point.getY() + 1);
            }
            atRest = true;
        }
    }
    public void moveLaterally(int direction, ArrayList<Piece> pieces) {
        for (Point point : this.getPoints()) {
            point.setX(point.getX() + direction);
        }
        if(collisionDetection(pieces)) {
            for (Point point : this.getPoints()) {
                point.setX(point.getX() - direction);
            }
        }
    }
    private boolean collisionDetection(ArrayList<Piece> pieces) {
        if(this.getMinimumX() < 0 || this.getMaximumX() > 6) {
            return true;
        }
        for (Piece piece : pieces) {
            for (Point point : piece.getPoints()) {
                for (Point testPoint : points) {
                    if (point.equals(testPoint)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
public class Advent17 {
    static int maxHeightForDrawing = 0;

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        String fileName = "input/input17.txt";
        long phase1Pieces = 2022L;
        long phase2Pieces = 1000000000000L;
        BufferedReader reader;
        String gasDirections = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            gasDirections = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long maxHeightPhase1 = playTetris(phase1Pieces, gasDirections, false, false, 0).getHeight();
        System.out.println("Phase 1 height: " + maxHeightPhase1);
        TetrisResults firstCycleStart = playTetris(phase2Pieces, gasDirections, true, true, 0);
        TetrisResults secondCycleStart = playTetris(phase2Pieces, gasDirections, true, true, firstCycleStart.getPieces());
        long cyclePieces = secondCycleStart.getPieces() - firstCycleStart.getPieces();
        long cycleCount = phase2Pieces / cyclePieces;
        long totalPieces = cyclePieces * cycleCount + firstCycleStart.getPieces();
        long cycleHeight = secondCycleStart.getHeight() - firstCycleStart.getHeight();
        long totalHeight = cycleHeight * cycleCount + firstCycleStart.getHeight();
        long overshoot = totalPieces - phase2Pieces; // Thanks to SimonBaars for this idea
        TetrisResults overshootPieces = playTetris(phase2Pieces, gasDirections , true, false, firstCycleStart.getPieces() - overshoot);
        System.out.println("Phase 2 height: " + (totalHeight - (firstCycleStart.getHeight() - overshootPieces.getHeight())));
        long currentTime = System.currentTimeMillis();
        double elapsedTime = (currentTime - startTime) / 1000.0;
        System.out.println("Time in seconds : " + elapsedTime);
    }

    private static TetrisResults playTetris(long pieces, String line, boolean bailout, boolean onlyOnGasReset, long bailOutVal) throws InterruptedException {
        Queue<Boolean> gasJet = new LinkedList<>();
        for(Character c : line.toCharArray()) {
            gasJet.add(c == '>'); // true if right, false if left
        }
        boolean gasReset;
        ArrayList<Piece> piecesList = new ArrayList<>();
        piecesList.add(new Piece(new Point[]{new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0),
                new Point(4, 0), new Point(5, 0), new Point(6, 0)}, TetrisColors.ANSI_WHITE));
        //try(ProgressBar pb = new ProgressBar("Tetris ", pieces)) {
            for(long i = 0; i < pieces; i++) {
                Piece piece = new Piece(getMaxHeight(piecesList), (int) (i % 5)); // iterate through the 5 pieces
                //pb.step();
                while(!piece.isAtRest()) {
                    gasReset = false;
                    if(gasJet.isEmpty()) { // refill gas jets
                        gasReset = true;
                        for(Character c : line.toCharArray()) {
                            gasJet.add(c == '>'); // true if right, false if left
                        }
                    }
                    if(bailout) {
                        if(onlyOnGasReset && gasReset) {
                            if(bailOutVal == 0 && i != 0) {
                                return new TetrisResults(getMaxHeight(piecesList), i);
                            } else if (i > bailOutVal) {
                                return new TetrisResults(getMaxHeight(piecesList), i);
                            }
                        } else if (!onlyOnGasReset && i == bailOutVal) {
                            return new TetrisResults(getMaxHeight(piecesList), i);
                        }
                    }
                    if(!bailout) {
                        drawTetris(piecesList, piece);
                    }

                    piece.moveLaterally(Boolean.TRUE.equals(gasJet.poll()) ? 1 : -1, piecesList);
                    piece.moveDown(piecesList);
                }
                piecesList.add(piece);
            }
        //}
        return new TetrisResults(getMaxHeight(piecesList));
    }

    public static int getMaxHeight(ArrayList<Piece> pieces) {
        int maxHeight = 0;
        for(Piece piece : pieces) {
            if(piece.getMaximumY() > maxHeight) {
                maxHeight = piece.getMaximumY();
            }
        }
        return maxHeight;
    }

    public static void drawTetris(ArrayList<Piece> pieces, Piece newPiece) throws InterruptedException {
        ArrayList<Piece> piecesCopy = new ArrayList<>();
        for(Piece piece : pieces) {
            piecesCopy.add(new Piece(piece.getPoints(), piece.getColor()));
        }
        piecesCopy.add(newPiece);

        int maxHeight = Math.max(getMaxHeight(piecesCopy), maxHeightForDrawing);
        maxHeightForDrawing = maxHeight;
        int maxWidth = 7;
        if(maxHeight < 40) {
            return;
        }
        Thread.sleep(80);
        char[][] tetris = new char[maxHeight + 1][maxWidth];
        String[][] tetrisColors = new String[maxHeight + 1][maxWidth];
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println(TetrisColors.ANSI_BLUE_BACKGROUND+TetrisColors.ANSI_WHITE+"     ╔══════════════╗"+TetrisColors.ANSI_RESET);
        System.out.println(TetrisColors.ANSI_BLUE_BACKGROUND+TetrisColors.ANSI_WHITE+"     ║1 2 3 4 5 6 7 ║"+TetrisColors.ANSI_RESET);
        System.out.println(TetrisColors.ANSI_BLUE_BACKGROUND+TetrisColors.ANSI_WHITE+"╔════╬══════════════╣"+TetrisColors.ANSI_RESET);
        for(int i = maxHeight; i > maxHeight - 20; i--) {
            for(int j = 0; j < maxWidth; j++) {
                tetris[i][j] = ' ';
                tetrisColors[i][j] = TetrisColors.ANSI_RESET;
            }
        }
        for(Piece piece : piecesCopy) {
            for(Point point : piece.getPoints()) {
                tetris[point.getY()][point.getX()] = '█';
                tetrisColors[point.getY()][point.getX()] = piece.getColor();
            }
        }
        for(int i = maxHeight; i > maxHeight - 20; i--) {
            System.out.print(TetrisColors.ANSI_BLUE_BACKGROUND+TetrisColors.ANSI_WHITE+"║"+String.format("%4d║", i)+TetrisColors.ANSI_RESET);
            for(int j = 0; j < maxWidth; j++) {
                System.out.print(TetrisColors.ANSI_BLUE_BACKGROUND+(tetris[i][j]!=' '?tetrisColors[i][j]:"")+tetris[i][j]+tetris[i][j]+TetrisColors.ANSI_RESET);
            }
            System.out.println(TetrisColors.ANSI_BLUE_BACKGROUND+TetrisColors.ANSI_WHITE+"║"+TetrisColors.ANSI_RESET);
        }
        System.out.println(TetrisColors.ANSI_BLUE_BACKGROUND+"╚════╩══════════════╝"+TetrisColors.ANSI_RESET);
    }

}
