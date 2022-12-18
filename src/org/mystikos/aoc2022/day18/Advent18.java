package org.mystikos.aoc2022.day18;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

final class Point {
    private final int x;
    private final int y;
    private final int z;
    public int getSides() {
        return sides;
    }
    public void setSides(int sides) {
        this.sides = sides;
    }
    public void decrementSides() {
        this.sides--;
    }
    private int sides;
    Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.sides = 6;
    }
    public int x() {
        return x;
    }
    public int y() {
        return y;
    }
    public int z() {
        return z;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Point) obj;
        return this.x == that.x &&
                this.y == that.y &&
                this.z == that.z;
    }
    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
    @Override
    public String toString() {
        return "Point[" +
                "x=" + x + ", " +
                "y=" + y + ", " +
                "z=" + z + ']';
    }
}

public class Advent18 {
    public static void main(String[] args) {
        String fileName = "input/input18.txt";
        ArrayList<Point> points = new ArrayList<>();
        BufferedReader reader;
        ProgressBarBuilder pbb = new ProgressBarBuilder()
                .setTaskName("Checking ")
                .setUnit(" Points", 1)
                .setStyle(ProgressBarStyle.COLORFUL_UNICODE_BAR)
                .showSpeed()
                .setMaxRenderedLength(100);
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                points.add(new Point(Integer.parseInt(line.split(",")[0]), Integer.parseInt(line.split(",")[1]), Integer.parseInt(line.split(",")[2])));
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        processPoints(points);
        int totalSides = 0;
        for (Point p : points) {
            totalSides += p.getSides();
        }
        System.out.println("Total sides: " + totalSides);
        int exteriorSides = 0;
        int buffer = 100;
        for (Point p : ProgressBar.wrap(points, pbb)) {
            p.setSides(floodFillTest(points, p.x(), p.y(), p.z(), buffer));
        }
        for (Point p : points) {
            exteriorSides += p.getSides();
        }
        System.out.println("Surface area: " + exteriorSides);
    }
    public static int floodFillTest(ArrayList<Point> points, int startX, int startY, int startZ, int buffer) {
        int exteriorSides = 0;
        if (!points.contains(new Point(startX - 1, startY, startZ))) {  // Left
            if(floodFill2(points, startX - 1, startY, startZ, buffer)) {
               exteriorSides++;
            }
        }
        if (!points.contains(new Point(startX + 1, startY, startZ))) {
            if(floodFill2(points, startX + 1, startY, startZ,buffer)) {
                exteriorSides++;
            }
        }
        if (!points.contains(new Point(startX, startY - 1, startZ))) {
            if(floodFill2(points, startX, startY - 1, startZ,buffer)) {
                exteriorSides++;
            }
        }
        if (!points.contains(new Point(startX, startY + 1, startZ))) {
            if(floodFill2(points, startX, startY + 1, startZ, buffer)) {
                exteriorSides++;
            }
        }
        if (!points.contains(new Point(startX, startY, startZ - 1))) {
            if(floodFill2(points, startX, startY, startZ - 1, buffer)) {
                exteriorSides++;
            }
        }
        if (!points.contains(new Point(startX, startY, startZ + 1))) {
            if(floodFill2(points, startX, startY, startZ + 1, buffer)) {
                exteriorSides++;
            }
        }
        return exteriorSides;
    }
    public static boolean floodFill2(ArrayList<Point> points, int startX, int startY, int startZ, int buffer) {
        Set<Point> pointSet = new HashSet<>(points);
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(startX, startY, startZ));
        while (!queue.isEmpty()) {
            Point p = queue.poll();
            int x = p.x();
            int y = p.y();
            int z = p.z();
            if (x > 0 && !pointSet.contains(new Point(x - 1, y, z))) {
                queue.add(new Point(x - 1, y, z));
                pointSet.add(new Point(x - 1, y, z));
            }
            if (x < buffer && !pointSet.contains(new Point(x + 1, y, z))) {
                queue.add(new Point(x + 1, y, z));
                pointSet.add(new Point(x + 1, y, z));
            }
            if (y > 0 && !pointSet.contains(new Point(x, y - 1, z))) {
                queue.add(new Point(x, y - 1, z));
                pointSet.add(new Point(x, y - 1, z));
            }
            if (y < buffer && !pointSet.contains(new Point(x, y + 1, z))) {
                queue.add(new Point(x, y + 1, z));
                pointSet.add(new Point(x, y + 1, z));
            }
            if (z > 0 && !pointSet.contains(new Point(x, y, z - 1))) {
                queue.add(new Point(x, y, z - 1));
                pointSet.add(new Point(x, y, z - 1));
            }
            if (z < buffer && !pointSet.contains(new Point(x, y, z + 1))) {
                queue.add(new Point(x, y, z + 1));
                pointSet.add(new Point(x, y, z + 1));
            }
            if(pointSet.size() > buffer*100) {
                return true;
            }
        }
        return false;
    }

    private static void processPoints(ArrayList<Point> points) {
        for (Point point : points) {
            for (Point otherPoint : points) {
                if (point.equals(otherPoint)) {
                    continue;
                }
                if (point.x() == otherPoint.x() && point.y() == otherPoint.y() && (point.z() == otherPoint.z()+1 || point.z() == otherPoint.z()-1)) {
                    point.decrementSides();
                }
                if (point.x() == otherPoint.x() && point.z() == otherPoint.z() && (point.y() == otherPoint.y()+1 || point.y() == otherPoint.y()-1)) {
                    point.decrementSides();
                }
                if (point.y() == otherPoint.y() && point.z() == otherPoint.z() && (point.x() == otherPoint.x()+1 || point.x() == otherPoint.x()-1)) {
                    point.decrementSides();
                }
            }
        }
    }
}
