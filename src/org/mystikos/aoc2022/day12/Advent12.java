package org.mystikos.aoc2022.day12;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.lang.Integer;

record PathFinder(int x, int y, int level, int distance, Stack<Point> history) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathFinder that = (PathFinder) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
public class Advent12 {
    private static final ArrayList<ArrayList<Integer>> map = new ArrayList<>();
    private static final ArrayList<ArrayList<PathFinder>> pathFinders = new ArrayList<>();
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        final int start=-13;
        final int end=-27;
        String fileName = "input/input12.txt";
        boolean phase2 = true;
        BufferedReader reader;
        ProgressBarBuilder pbb = new ProgressBarBuilder()
                .setTaskName("Pathfinding")
                .setUnit(" Paths", 1)
                .setStyle(ProgressBarStyle.COLORFUL_UNICODE_BAR)
                .showSpeed()
                .setMaxRenderedLength(100);
        try {
            reader = new BufferedReader(new FileReader(fileName));
            int row = 0;
            String line = reader.readLine();
            while (line != null) {
                map.add(new ArrayList<>());
                for(int x = 0; x < line.length(); x++) {
                    if(((int)line.charAt(x))-96 == start) {
                        map.get(row).add(0);
                        ArrayList<PathFinder> p = new ArrayList<>();
                        p.add(new PathFinder(x, row, 0, 0, new Stack<>()));
                        pathFinders.add(p);
                    } else if(phase2 && ((int)line.charAt(x))-96 == 1) {
                        map.get(row).add(Math.abs((int) line.charAt(x) - 96));
                        ArrayList<PathFinder> p = new ArrayList<>();
                        p.add(new PathFinder(x, row, 1, 0, new Stack<>()));
                        pathFinders.add(p);
                    } else if(((int)line.charAt(x))-96 == end) {
                        map.get(row).add(27);
                    } else {
                        map.get(row).add(Math.abs((int) line.charAt(x) - 96));
                    }
                }
                row++;
                line = reader.readLine();
            }
            int counter = 0;
            TreeSet<Integer> shortestPath = new TreeSet<>();
            for(ArrayList<PathFinder> groupOfPaths : ProgressBar.wrap(pathFinders, pbb)) {
                boolean found = false;
                while(!found && groupOfPaths.size()!=0) {
                    ArrayList<PathFinder> newPathFinders = new ArrayList<>();
                    for (PathFinder p : groupOfPaths) {
                        if (p.level() == 27) {
                            shortestPath.add(p.distance());
                            found = true;
                            break;
                        }
                        for (PathFinder pc : checkAround(p)) {
                            if (!newPathFinders.contains(pc)) {
                                newPathFinders.add(pc);
                            }
                        }

                    }
                    groupOfPaths.clear();
                    groupOfPaths.addAll(newPathFinders);
                }
            }
            System.out.println("Shortest Path = "+ shortestPath.first());
            long currentTime = System.currentTimeMillis();
            double elapsedTime = (currentTime - startTime) / 1000.0;
            System.out.println("Time in seconds : " + elapsedTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   private static ArrayList<PathFinder> checkAround(PathFinder p) {
       int x=p.x();
       int y=p.y();
       int level=p.level()+1;
       @SuppressWarnings("unchecked") Stack<Point> existingHistory = (Stack<Point>) p.history().clone();
       existingHistory.add(new Point(p.x(),p.y()));
       ArrayList<PathFinder> newPathFinders = new ArrayList<>();
       if(y>0 && !existingHistory.contains(new Point(x,y-1)) && level >= map.get(y-1).get(x)) {
           newPathFinders.add(new PathFinder(x, y - 1, map.get(y - 1).get(x), p.distance() + 1, existingHistory));
       }
       if(y<map.size()-1 && !existingHistory.contains(new Point(x,y+1)) && level >= map.get(y+1).get(x)) {
           newPathFinders.add(new PathFinder(x, y + 1, map.get(y + 1).get(x), p.distance() + 1, existingHistory));
       }
       if(x>0 && !existingHistory.contains(new Point(x-1,y)) && level >= map.get(y).get(x-1)) {
           newPathFinders.add(new PathFinder(x-1, y, map.get(y).get(x-1), p.distance() + 1, existingHistory));
       }
       if(x<map.get(y).size()-1 && !existingHistory.contains(new Point(x+1,y)) && level >= map.get(y).get(x+1)) {
           newPathFinders.add(new PathFinder(x+1, y, map.get(y).get(x+1), p.distance() + 1, existingHistory));
       }
       return newPathFinders;
   }
}
