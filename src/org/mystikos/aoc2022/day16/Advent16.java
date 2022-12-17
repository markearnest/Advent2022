package org.mystikos.aoc2022.day16;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import me.tongfei.progressbar.*;

final class Valve {
    private final String valveName;
    private long flowRate;
    private final String[] adjacentValves;
    Valve(String valveName, long flowRate, String[] adjacentValves) {
        this.valveName = valveName;
        this.flowRate = flowRate;
        this.adjacentValves = adjacentValves;
    }
    public void clearFlowRate() {
        this.flowRate = 0;
    }
    public String valveName() {
        return valveName;
    }
    public long flowRate() {
        return flowRate;
    }
    public String[] adjacentValves() {
        return adjacentValves;
    }
}
record ValvePath(Map<String, Long> openValves, Valve currentValve, long totalFlow) {
}
public class Advent16 {
    @SuppressWarnings("unused")
    public static <Map> void main(String[] args) {
        long startTime = System.currentTimeMillis();
        HashMap<String, Valve> valves = new HashMap<>();
        String fileName = "input/input16.txt";
        BufferedReader reader;
        Set<String> valvesToOpen = new HashSet<>();
        int phase1Minutes = 30;
        int phase2Minutes = 26;
        ProgressBarBuilder pbb = new ProgressBarBuilder()
                .setTaskName("Pathfinding")
                .setUnit(" Paths", 1)
                .setStyle(ProgressBarStyle.COLORFUL_UNICODE_BAR)
                .showSpeed()
                .setMaxRenderedLength(100);
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                String[] splitLine = line.split(";");
                String[] adjacentValves = splitLine[1].substring(splitLine[1].indexOf("valve") + 6).trim().split(", ");
                Valve valve = new Valve(splitLine[0].split(" ")[1].trim(), Long.parseLong(splitLine[0].split(" ")[4].trim().substring(splitLine[0].split(" ")[4].trim().indexOf('=') + 1).trim()), adjacentValves);
                valves.put(splitLine[0].split(" ")[1].trim(), valve);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Set<ValvePath> statesToCheck = new HashSet<>();
        Set<ValvePath> statesToCheckPhase2 = new HashSet<>();
        ValvePath initialValvePath = new ValvePath(new HashMap<>(), valves.get("AA"), 0);
        statesToCheck.add(initialValvePath);
        for(int i = 0; i < phase1Minutes; i++) {
            System.out.println("Minute: " + (i+1) + " - "+ statesToCheck.size() + " calculations required");
            Set<ValvePath> newStatesToCheck = new HashSet<>();
            for(ValvePath valvePath : ProgressBar.wrap(statesToCheck, pbb)) {
                long totalFlow = valvePath.openValves().values().stream().mapToLong(e -> e).sum() + valvePath.totalFlow();
                if (valvePath.currentValve().flowRate() > 0 && !valvePath.openValves().containsKey(valvePath.currentValve())) {
                    HashMap
                            newOpenValves = new HashMap(valvePath.openValves());
                    newOpenValves.put(valvePath.currentValve(), valvePath.currentValve().flowRate());
                    newStatesToCheck.add(new ValvePath(newOpenValves, valvePath.currentValve(), totalFlow));
                }
                // I cannot take credit on this, someone clued me in on the use of streams for this. Otherwise I would have used a for loop.
                Arrays.stream(valvePath.currentValve().adjacentValves()).forEach(name -> newStatesToCheck.add(new ValvePath(valvePath.openValves(), valves.get(name), totalFlow)));
            }
            System.out.println();
            statesToCheck = newStatesToCheck;
            if(i==phase2Minutes-1) {
                statesToCheckPhase2 = newStatesToCheck;
            }
        }
        long maxFlowPhase1 = 0;
        long maxFlowPhase2Round1 = 0;
        long maxFlowPhase2Round2 = 0;
        for(ValvePath valvePath : statesToCheck) {
            if(valvePath.totalFlow() > maxFlowPhase1) {
                maxFlowPhase1 = valvePath.totalFlow();
            }
        }
        for(ValvePath valvePath : statesToCheckPhase2) {
            if(valvePath.totalFlow() > maxFlowPhase2Round1) {
                maxFlowPhase2Round1 = valvePath.totalFlow();
            }
        }
        ValvePath valvePathToFilter = null;
        for(ValvePath valvePath : statesToCheckPhase2) {
            if(valvePath.totalFlow() == maxFlowPhase2Round1) {
                valvePathToFilter = valvePath;
            }
        }
        Object[] toClear = new Object[0];
        if (valvePathToFilter != null) {
            toClear = valvePathToFilter.openValves().keySet().toArray();
        }
        for (Object valve : toClear) { // clear out already open valves
            System.out.println("Clearing " + ((Valve) valve).valveName());
            valves.get(((Valve) valve).valveName()).clearFlowRate();
        }
        initialValvePath = new ValvePath(new HashMap<>(), valves.get("AA"), 0);
        statesToCheck = new HashSet<>();
        statesToCheck.add(initialValvePath);
        for(int i = 0; i < phase2Minutes; i++) {
            System.out.println("Minute: " + (i+1) + " - "+ statesToCheck.size() + " calculations required");
            Set<ValvePath> newStatesToCheck = new HashSet<>();
            for(ValvePath valvePath : ProgressBar.wrap(statesToCheck, pbb)) {
                long totalFlow = valvePath.openValves().values().stream().mapToLong(e -> e).sum() + valvePath.totalFlow();
                if (valvePath.currentValve().flowRate() > 0 && !valvePath.openValves().containsKey(valvePath.currentValve())) {
                    HashMap newOpenValves = new HashMap(valvePath.openValves());
                    newOpenValves.put(valvePath.currentValve(), valvePath.currentValve().flowRate());
                    newStatesToCheck.add(new ValvePath(newOpenValves, valvePath.currentValve(), totalFlow));
                }
                Arrays.stream(valvePath.currentValve().adjacentValves()).forEach(name -> newStatesToCheck.add(new ValvePath(valvePath.openValves(), valves.get(name), totalFlow)));
            }
            System.out.println();
            statesToCheck = newStatesToCheck;
        }

        for(ValvePath valvePath : statesToCheck) {
            if(valvePath.totalFlow() > maxFlowPhase2Round2) {
                maxFlowPhase2Round2 = valvePath.totalFlow();
            }
        }
        maxFlowPhase2Round2 += maxFlowPhase2Round1;
        System.out.println();
        System.out.println("Phase 1: " + maxFlowPhase1);
        System.out.println("Phase 2: " + maxFlowPhase2Round2);
        long currentTime = System.currentTimeMillis();
        double elapsedTime = (currentTime - startTime) / 1000.0;
        System.out.println("Time in seconds : " + elapsedTime);
    }
}
