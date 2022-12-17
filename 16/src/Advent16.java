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
        String fileName = "16/input.txt";
        BufferedReader reader;
        boolean phase2 = false; // set to true for phase 2
        Set<String> valvesToOpen = new HashSet<>();
        int minutes = phase2 ? 26 : 30;
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
        ValvePath initialValvePath = new ValvePath(new HashMap<>(), valves.get("AA"), 0);
        statesToCheck.add(initialValvePath);
        for(int i = 0; i < minutes; i++) {
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
        }
        long maxFlow = 0;
        for(ValvePath valvePath : statesToCheck) {
            if(valvePath.totalFlow() > maxFlow) {
                maxFlow = valvePath.totalFlow();
            }
        }
        ValvePath valvePathToFilter = null;
        for(ValvePath valvePath : statesToCheck) {
            if(valvePath.totalFlow() == maxFlow) {
                valvePathToFilter = valvePath;
            }
        }
        if (phase2) { // I'm just running the logic above again as the elephant now
            Object[] toClear = valvePathToFilter.openValves().keySet().toArray();
            for (Object valve : toClear) { // clear out already open valves
                System.out.println("Clearing " + ((Valve) valve).valveName());
                valves.get(((Valve) valve).valveName()).clearFlowRate();
            }
            initialValvePath = new ValvePath(new HashMap<>(), valves.get("AA"), 0);
            statesToCheck = new HashSet<>();
            statesToCheck.add(initialValvePath);
            for(int i = 0; i < minutes; i++) {
                System.out.println("Minute: " + (i+1) + " - "+ statesToCheck.size() + " calculations required");
                Set<ValvePath> newStatesToCheck = new HashSet<>();
                for(ValvePath valvePath : ProgressBar.wrap(statesToCheck, pbb)) {
                    long totalFlow = valvePath.openValves().values().stream().mapToLong(e -> e).sum() + valvePath.totalFlow();
                    if(valvePath.openValves().size() == valvesToOpen.size()) { // If all valves are open
                        newStatesToCheck.add(new ValvePath(valvePath.openValves(), valves.get("AA"), totalFlow));
                    }
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
            long maxFlow2 = 0;
            for(ValvePath valvePath : statesToCheck) {
                if(valvePath.totalFlow() > maxFlow2) {
                    maxFlow2 = valvePath.totalFlow();
                }
            }
            maxFlow += maxFlow2;
        }
        System.out.println();
        System.out.println("Phase " + (phase2 ? "2 " : "1 ") + maxFlow);
        long currentTime = System.currentTimeMillis();
        double elapsedTime = (currentTime - startTime) / 1000.0;
        System.out.println("Time in seconds : " + elapsedTime);
    }
}
