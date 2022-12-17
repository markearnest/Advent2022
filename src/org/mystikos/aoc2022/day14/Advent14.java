package org.mystikos.aoc2022.day14;

import java.io.*;
import java.util.HashMap;

record CavePoint(int x, int y) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CavePoint cavePoint = (CavePoint) o;
        return x == cavePoint.x && y == cavePoint.y;
    }
}
public class Advent14 {
    public static void main(String[] args) {
        String fileName = "input/input14.txt";
        int phase1 = 0;
        int phase2 = 0;
        BufferedReader reader;
        HashMap<CavePoint, Character> points = new HashMap<>();
        for(int x = 0;x<1000;x++) { // supposed to be infinite, but my patience isn't. This is big enough
            for(int y = 0;y<1000;y++) {
                points.put(new CavePoint(x,y), '.');
            }
        }
        int lowestRock = 0;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                String[] endPoints = line.split("->");
                for (int rockLine = 0; rockLine < endPoints.length - 1; rockLine++) {
                    String[] point1 = endPoints[rockLine].split(",");
                    String[] point2 = endPoints[rockLine + 1].split(",");
                    CavePoint p1 = new CavePoint(Integer.parseInt(point1[0].trim()), Integer.parseInt(point1[1].trim()));
                    CavePoint p2 = new CavePoint(Integer.parseInt(point2[0].trim()), Integer.parseInt(point2[1].trim()));
                    lowestRock = Math.max(Math.max(p1.y(), p2.y()), lowestRock);
                    for (int y = Math.min(p1.y(), p2.y()); y <= Math.max(p1.y(), p2.y()); y++) {
                        for (int x = Math.min(p1.x(), p2.x()); x <= Math.max(p1.x(), p2.x()); x++) {
                            points.put(new CavePoint(x, y), '#');
                        }
                    }
                }
                line = reader.readLine();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        int bedrock = lowestRock + 2;
        CavePoint sand = new CavePoint(0, 0);
        while (!sand.equals(new CavePoint(500, 0))) {
            sand = new CavePoint(500,0);
            outerLoop:
            while (true) {
                int y = sand.y() + 1;
                for (int sandRestingPoint : new int[]{0, -1, 1}) {
                    int x = sand.x() + sandRestingPoint;
                    if (y < bedrock && points.get(new CavePoint(x, y)) == '.') {
                        sand = new CavePoint(x, y);
                        continue outerLoop;
                    }
                }
                if (phase1 == 0 && sand.y() > lowestRock) {
                    phase1 = phase2;
                }
                points.put(sand, 'o');
                break;
            }
            phase2++;
        }
        System.out.println("Phase 1: " + phase1);
        System.out.println("Phase 2: " + phase2);
    }
}
