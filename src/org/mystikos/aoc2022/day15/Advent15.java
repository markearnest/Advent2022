package org.mystikos.aoc2022.day15;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.LongStream;


record SensorBeacon(Coordinate sensor, Coordinate beacon) {
    public long manhattanDistance() { // should have realized earlier it was in the question for a reason
        return Math.abs(sensor.x() - beacon.x()) + Math.abs(sensor.y() - beacon.y());
    }
}
record Coordinate(long x, long y) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate coordinate = (Coordinate) o;
        return x == coordinate.x && y == coordinate.y;
    }
    @Contract("_ -> new")
    public static @NotNull Coordinate parseXY(@NotNull String xy) {
        String[] point = xy.split(",");
        return new Coordinate(Long.parseLong(point[0].trim().substring(point[0].trim().indexOf('x') + 2)),
                Long.parseLong(point[1].trim().substring(point[1].trim().indexOf('y') + 2)));
    }
}
public class Advent15 {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String fileName = "input/input15.txt";
        ProgressBarBuilder pbb = new ProgressBarBuilder()
                .setTaskName("Scanning ")
                .setUnit(" coordinates", 1)
                .setStyle(ProgressBarStyle.COLORFUL_UNICODE_BAR)
                .showSpeed()
                .setMaxRenderedLength(100);
        ArrayList<SensorBeacon> sensorBeacons = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                String[] splitLine = line.split(":");
                sensorBeacons.add(new SensorBeacon(Coordinate.parseXY(splitLine[0]), Coordinate.parseXY(splitLine[1])));
                line = reader.readLine();
            }
            reader.close();
            int phase1Line = 2000000;
            int phase2boundry = 4000000;
            Set<Long> phase1CoordinatesCovered = new HashSet<>();
            int phase1Answer;
            BigInteger phase2Answer;
            for(SensorBeacon sensorBeacon : ProgressBar.wrap(sensorBeacons,pbb)) {
                long manhattanDistance = sensorBeacon.manhattanDistance();
                long sensorY = Math.abs(sensorBeacon.sensor().y() - phase1Line);
                LongStream range = LongStream.range(sensorBeacon.sensor().x() - manhattanDistance + sensorY,
                        sensorBeacon.sensor().x() + manhattanDistance - sensorY);
                range.forEach(phase1CoordinatesCovered::add);
            }
            phase1Answer = phase1CoordinatesCovered.size();
            System.out.println("Phase 1 answer: " + phase1Answer);
            Map<Coordinate, Integer> phase2Map = possiblePoints(sensorBeacons, phase2boundry, pbb);
            for (Coordinate p : ProgressBar.wrap(phase2Map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())), pbb).
                    map(Map.Entry::getKey).toList()) { // not all me, got the idea for ordering these by frequency for performance from a mastodon post
                if(!isNotPossiblePoint(new Coordinate(p.x(), p.y()), sensorBeacons)) {
                    phase2Answer = new BigInteger(String.valueOf(phase2boundry)).multiply(new BigInteger(String.valueOf(p.x()))).add(new BigInteger(String.valueOf(p.y())));
                    System.out.println("Phase 2 answer: " + phase2Answer);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long currentTime = System.currentTimeMillis();
        double elapsedTime = (currentTime - startTime) / 1000.0;
        System.out.println("Time in seconds : " + elapsedTime);
    }
    private static boolean isNotPossiblePoint(Coordinate point, @NotNull ArrayList<SensorBeacon> sensorBeacons) {
        for (SensorBeacon sensorBeacon : sensorBeacons) {
            if (sensorBeacon.manhattanDistance() > Math.abs(sensorBeacon.sensor().x() - point.x()) +
                    Math.abs(sensorBeacon.sensor().y() - point.y())) {
                return true;
            }
        }
        return false;
    }
    private static @NotNull Map<Coordinate, Integer> possiblePoints(@NotNull ArrayList<SensorBeacon> sensorBeacons, int boundry, ProgressBarBuilder pbb) {
        Map<Coordinate, Integer> possiblePoints = new HashMap<>();
        for (SensorBeacon sensorBeacon : ProgressBar.wrap(sensorBeacons,pbb.setTaskName("Possible Points ").setUnit(" points", 1))) {
            long manhattanDistance = sensorBeacon.manhattanDistance();
            for(int i=0;i <manhattanDistance;i++) {
                if(0 <= sensorBeacon.sensor().x() - manhattanDistance + i - 1 && sensorBeacon.sensor().x() - manhattanDistance + i - 1 <= boundry &&
                    0 <= sensorBeacon.sensor().y() + i && sensorBeacon.sensor().y() + i <= boundry) {
                        possiblePoints.put(new Coordinate(sensorBeacon.sensor().x() - manhattanDistance + i - 1, sensorBeacon.sensor().y() + i),
                        possiblePoints.get(new Coordinate(sensorBeacon.sensor().x() - manhattanDistance + i - 1, sensorBeacon.sensor().y() + i)) != null ?
                        possiblePoints.get(new Coordinate(sensorBeacon.sensor().x() - manhattanDistance + i - 1, sensorBeacon.sensor().y() + i)) + 1 : 1);
                }
                if(0 <= sensorBeacon.sensor().x() - manhattanDistance + i + 1 && sensorBeacon.sensor().x() - manhattanDistance + i + 1 <= boundry &&
                        0 <= sensorBeacon.sensor().y() - i && sensorBeacon.sensor().y() - i <= boundry) {
                        possiblePoints.put(new Coordinate(sensorBeacon.sensor().x() - manhattanDistance + i + 1, sensorBeacon.sensor().y() - i),
                        possiblePoints.get(new Coordinate(sensorBeacon.sensor().x() - manhattanDistance + i + 1, sensorBeacon.sensor().y() - i)) != null ?
                        possiblePoints.get(new Coordinate(sensorBeacon.sensor().x() - manhattanDistance + i + 1, sensorBeacon.sensor().y() - i)) + 1 : 1);
                }
                if(0 <= sensorBeacon.sensor().x() + i && sensorBeacon.sensor().x() + i <= boundry &&
                        0 <= sensorBeacon.sensor().y() - manhattanDistance - i - 1 && sensorBeacon.sensor().y() - manhattanDistance - i - 1 <= boundry) {
                        possiblePoints.put(new Coordinate(sensorBeacon.sensor().x() + i, sensorBeacon.sensor().y() - manhattanDistance - i - 1),
                        possiblePoints.get(new Coordinate(sensorBeacon.sensor().x() + i, sensorBeacon.sensor().y() - manhattanDistance - i - 1)) != null ?
                        possiblePoints.get(new Coordinate(sensorBeacon.sensor().x() + i, sensorBeacon.sensor().y() - manhattanDistance - i - 1)) + 1 : 1);
                }
                if(0 <= sensorBeacon.sensor().x() - i && sensorBeacon.sensor().x() - i <= boundry &&
                        0 <= sensorBeacon.sensor().y() - manhattanDistance + i + 1 && sensorBeacon.sensor().y() - manhattanDistance + i + 1 <= boundry) {
                        possiblePoints.put(new Coordinate(sensorBeacon.sensor().x() - manhattanDistance + i + 1, sensorBeacon.sensor().y() - i),
                        possiblePoints.get(new Coordinate(sensorBeacon.sensor().x() - manhattanDistance + i + 1, sensorBeacon.sensor().y() - i)) != null ?
                        possiblePoints.get(new Coordinate(sensorBeacon.sensor().x() - manhattanDistance + i + 1, sensorBeacon.sensor().y() - i)) + 1 : 1);
                }
            }
        }
        return possiblePoints;
    }
}
