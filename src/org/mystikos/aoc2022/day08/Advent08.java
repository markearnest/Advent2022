package org.mystikos.aoc2022.day08;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.lang.Integer;

class Coordinates implements Comparable<Coordinates> {
    int col;
    int row;
    public Coordinates(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return col == that.col && row == that.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }

    @Override
    public String toString() {
        return this.col + ":" + this.row;
    }

    @Override
    public int compareTo(Coordinates o) {
        if(this.equals(o)) return 0;
        if(this.col > o.getCol()) return 1;
        if(this.col == o.getCol() && this.row > o.getRow()) return 1;
        return -1;
    }
}
public class Advent08 {
    public static void main(String[] args) {
        int maxrow;
        int maxcol;
        Map<Coordinates, Integer> forestMap = new HashMap<>();
        SortedSet<Coordinates> visible = new TreeSet<>();
        String fileName = "input/input08.txt";
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            maxcol = line.length();
            int row = 0;
            int col;
            int maxHeight;
            while (line != null) { // read in each line and check from left
                maxHeight = 0;
                visible.add(new Coordinates(0, row));
                for (int x = 0; x < line.length(); x++) {
                    int height = Integer.parseInt(line.substring(x, x+1));
                    if (height > maxHeight) {
                        visible.add(new Coordinates(x, row));
                        maxHeight = height;
                    }
                    forestMap.put(new Coordinates(x, row), height);
                }
                line = reader.readLine();
                row++;
            }
            maxrow = row ;
            for(row=0; row < maxrow ; row++) { // check from the right
                maxHeight = 0;
                visible.add(new Coordinates(maxcol-1, row));
                for(col=maxcol-1;col>=0;col--) {
                    int height = forestMap.get(new Coordinates(col, row));
                    if (height > maxHeight) {
                        visible.add(new Coordinates(col, row));
                        maxHeight = height;
                    }
                }
            }
            for (col = 0; col < maxcol; col++) { // check from top
                maxHeight = 0;
                visible.add(new Coordinates(col, 0));
                for (row = 0; row < maxrow; row++) {
                    int height = forestMap.get(new Coordinates(col, row));
                    if (height > maxHeight) {
                        visible.add(new Coordinates(col, row));
                        maxHeight = height;
                    }
                }
            }
            for (col = 0; col < maxcol; col++) { // check from bottom
                maxHeight = 0;
                visible.add(new Coordinates(col, maxrow - 1));
                for (row = maxrow - 1; row >= 0; row--) {
                    int height = forestMap.get(new Coordinates(col, row));
                    if (height > maxHeight) {
                        visible.add(new Coordinates(col, row));
                        maxHeight = height;
                    }
                }
            }
            System.out.println("Trees Visible = " + visible.size());
            SortedSet<Integer> scoreSet = new TreeSet<>();
            for(Coordinates c : forestMap.keySet()) {
                scoreSet.add(viewScore(c.getCol(), c.getRow(), forestMap.get(c) , forestMap, maxrow, maxcol));
            }
            System.out.println("Best Score = " + scoreSet.last());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static int viewScore(int col, int row, int height, Map<Coordinates, Integer> forestMap, int maxrow, int maxcol) {
        int n = 0; int s = 0; int e = 0; int w = 0;
        for(int r = row-1; r >= 0; r--) { // check north
            n++; if(forestMap.get(new Coordinates(col,r)) >= height) break;
        }
        for(int r = row+1; r < maxrow; r++) { // check south
            s++; if(forestMap.get(new Coordinates(col,r)) >= height) break;
        }
        for(int c = col+1; c < maxcol; c++) { // check east
            e++; if(forestMap.get(new Coordinates(c,row)) >= height) break;
        }
        for(int c = col-1; c >= 0; c--) { // check west
            w++; if(forestMap.get(new Coordinates(c,row)) >= height) break;
        }
        return n*s*e*w;
    }



}
