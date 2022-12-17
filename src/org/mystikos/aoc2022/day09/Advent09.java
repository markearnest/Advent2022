package org.mystikos.aoc2022.day09;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.lang.Integer;

class Rope {
    private Point head;
    private Point tail;
    Rope() {
        head = new Point(0,0);
        tail = new Point(0,0);
    }
    private long getDistance() {
        return Math.round(head.distance(tail));
    }
    private boolean isTouching( ) {
        return getDistance() >= 2;
    }
    private boolean isDiagonal() {
        return Math.round(head.distance(tail)) != head.distance(tail);
    }
    private void moveHead(char direction) {
        head = movePointStraight(this.head, direction);
    }
    private Point movePointStraight (Point pt, char direction) {
        return switch (direction) {
            case 'U' -> new Point((int) pt.getX(), (int) (pt.getY() + 1));
            case 'D' -> new Point((int) pt.getX(), (int) (pt.getY() - 1));
            case 'L' -> new Point((int) pt.getX() - 1, (int) (pt.getY()));
            case 'R' -> new Point((int) pt.getX() + 1, (int) (pt.getY()));
            default -> pt;
        };
    }
    private int yOffset() {
        return (int) (head.getY() - tail.getY());
    }
    private int xOffset() {
        return (int) (head.getX() - tail.getX());
    }
    private boolean isyOffMore() {
        return yOffset() > xOffset();
    }
    private boolean isYOff() {
        return yOffset()!=0;
    }
    private Point moveTailIfNecessary() {
        Point newTail;
        if(isTouching() && !isDiagonal()) {
            if (isYOff()) {
                if (yOffset() > 0) {
                    newTail = movePointStraight(this.tail, 'U');
                } else {
                    newTail = movePointStraight(this.tail, 'D');
                }
            } else {
                if (xOffset() > 0) {
                    newTail = movePointStraight(this.tail, 'R');
                } else {
                    newTail = movePointStraight(this.tail, 'L');
                }
            }
            tail = newTail;
            return newTail;
        } else if(isTouching() && isDiagonal()) {
            if (isyOffMore()) {
                if (yOffset() > 0) {
                    newTail = movePointStraight(this.tail, 'U');
                } else {
                    newTail = movePointStraight(this.tail, 'D');
                }
                if(newTail.getX() < head.getX()) {
                    newTail.move((int) newTail.getX() +1, (int) newTail.getY());
                } else {
                    newTail.move((int) newTail.getX() -1, (int) newTail.getY());
                }
            } else {
                if(xOffset() > 0) {
                    newTail = movePointStraight(this.tail, 'R');
                } else {
                    newTail = movePointStraight(this.tail, 'L');
                }
                if(newTail.getY() < head.getY()) {
                    newTail.move((int) newTail.getX(), (int) newTail.getY()+1);
                } else {
                    newTail.move((int) newTail.getX(), (int) newTail.getY()-1);
                }
            }
            tail = newTail;
            return newTail;
        } else {
            return null;
        }
    }
    public Point move(char direction) {
        moveHead(direction);
        return moveTailIfNecessary();
    }
    public Point move(Point p) {
        head.setLocation(p);
        return moveTailIfNecessary();
    }

    public Point getTail() {
        return tail;
    }
}
public class Advent09 {
    public static void main(String[] args) {
        String fileName = "input/input09.txt";
        int ropeLength=10;
        Rope[] ropeArray = new Rope[ropeLength];
        HashSet<Point>[] tailPositions = new HashSet[ropeLength];
        for(int x = 0;x < ropeLength; x++) {
            ropeArray[x] = new Rope();
            tailPositions[x] = new HashSet<>();
            tailPositions[x].add(new Point(0,0));
        }
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                for(int x = 0; x < Integer.parseInt(line.split(" ")[1]); x++) {
                    Point newTail = ropeArray[0].move(line.split(" ")[0].charAt(0));
                    if (newTail != null) {
                        tailPositions[0].add(newTail);
                    }
                    for (int y = 1; y < ropeLength; y++) {
                        newTail = ropeArray[y].move(ropeArray[y - 1].getTail());
                        if (newTail == null) {
                            break;
                        } else {
                            tailPositions[y].add(newTail);
                        }
                    }
                }
                line = reader.readLine();
            }
            System.out.println("Total positions occupied by tail: "+tailPositions[ropeLength-2].size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}