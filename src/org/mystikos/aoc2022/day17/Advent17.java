package org.mystikos.aoc2022.day17;

import me.tongfei.progressbar.ProgressBar;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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
    private boolean atRest;
    public Piece(Point[] points) {
        this.points = points;
        atRest = false;
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
            }
            case 1 -> { // plus
                points[0] = new Point(lowerLeft.getX() + 1, lowerLeft.getY());
                points[1] = new Point(lowerLeft.getX(), lowerLeft.getY() + 1);
                points[2] = new Point(lowerLeft.getX() + 1, lowerLeft.getY() + 1);
                points[3] = new Point(lowerLeft.getX() + 2, lowerLeft.getY() + 1);
                points[4] = new Point(lowerLeft.getX() + 1, lowerLeft.getY() + 2);
            }
            case 2 -> { // flipped L
                points[0] = lowerLeft;
                points[1] = new Point(lowerLeft.getX() + 1, lowerLeft.getY());
                points[2] = new Point(lowerLeft.getX() + 2, lowerLeft.getY());
                points[3] = new Point(lowerLeft.getX() + 2, lowerLeft.getY() + 1);
                points[4] = new Point(lowerLeft.getX() + 2, lowerLeft.getY() + 2);
            }
            case 3 -> { // |
                points[0] = lowerLeft;
                points[1] = new Point(lowerLeft.getX(), lowerLeft.getY() + 1);
                points[2] = new Point(lowerLeft.getX(), lowerLeft.getY() + 2);
                points[3] = new Point(lowerLeft.getX(), lowerLeft.getY() + 3);
            }
            case 4 -> { // square
                points[0] = lowerLeft;
                points[1] = new Point(lowerLeft.getX() + 1, lowerLeft.getY());
                points[2] = new Point(lowerLeft.getX(), lowerLeft.getY() + 1);
                points[3] = new Point(lowerLeft.getX() + 1, lowerLeft.getY() + 1);
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
    public static void main(String[] args) {
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

    private static TetrisResults playTetris(long pieces, String line, boolean bailout, boolean onlyOnGasReset, long bailOutVal) {
        Queue<Boolean> gasJet = new LinkedList<>();
        for(Character c : line.toCharArray()) {
            gasJet.add(c == '>'); // true if right, false if left
        }
        boolean gasReset;
        ArrayList<Piece> piecesList = new ArrayList<>();
        piecesList.add(new Piece(new Point[]{new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0),
                new Point(4, 0), new Point(5, 0), new Point(6, 0)}));
        try(ProgressBar pb = new ProgressBar("Tetris ", pieces)) {
            for(long i = 0; i < pieces; i++) {
                Piece piece = new Piece(getMaxHeight(piecesList), (int) (i % 5)); // iterate through the 5 pieces
                pb.step();
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
                    piece.moveLaterally(Boolean.TRUE.equals(gasJet.poll()) ? 1 : -1, piecesList);
                    piece.moveDown(piecesList);
                }
                piecesList.add(piece);
            }
        }
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

}
