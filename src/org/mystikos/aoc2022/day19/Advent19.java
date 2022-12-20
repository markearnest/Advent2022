package org.mystikos.aoc2022.day19;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import java.io.BufferedReader;
import java.util.*;
import java.util.stream.Collectors;

record RobotRequirements(int type, Map<Integer, Integer> requires) {
    public final static String ORE = "ore";
    public final static String CLAY = "clay";
    public final static String OBSIDIAN = "obsidian";
    public final static String GEODE = "geode";
    public final static Map<String, Integer> MATERIAL = new HashMap<>() {{
        put("ore", 0);
        put("clay", 1);
        put("obsidian", 2);
        put("geode", 3);
    }};

    public int getRequires(int material) {
        return requires.getOrDefault(material, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RobotRequirements robot = (RobotRequirements) o;
        return Objects.equals(requires, robot.requires);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requires);
    }
}

record Robots(int[] robots) {
    public void add(int robot) {
        robots[robot]++;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Robots robots1 = (Robots) o;
        return Arrays.equals(robots, robots1.robots);
    }
    @Override
    public int hashCode() {
        return Arrays.hashCode(robots);
    }
    public int get(Integer robot) {
        return robots[robot];
    }
}

class Resources {
    final int[] resources;
    public Resources(int[] resources) {
        this.resources = resources;
    }
    public Resources() {
        this(new int[RobotRequirements.MATERIAL.size()]);
    }
    public int get(int material) {
        return resources[material];
    }
    public void add(int material, int amount) {
        resources[material] += amount;
    }
    public boolean canMake(RobotRequirements robot) {
        for (Map.Entry<Integer, Integer> entry : robot.requires().entrySet()) {
            if (resources[entry.getKey()] < entry.getValue()) {
                return false;
            }
        }
        return true;
    }
    public int make(RobotRequirements robot) {
        for (Map.Entry<Integer, Integer> entry : robot.requires().entrySet()) {
            resources[entry.getKey()] -= entry.getValue();
        }
        return robot.type();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resources resources1 = (Resources) o;
        return Arrays.equals(resources, resources1.resources);
    }
    @Override
    public int hashCode() {
        return Arrays.hashCode(resources);
    }
}

record PossibleOutcomes(List<Robots> possibleRobots, List<Resources> possibleResources) {
    public void addPossibleRobots(Robots possibleRobots) {
        this.possibleRobots.add(possibleRobots);
    }

    public void addPossibleResources(Resources possibleResources) {
        this.possibleResources.add(possibleResources);
    }

    public void AddPossibleOutcomes(PossibleOutcomes possibleOutcomes) {
        possibleRobots.addAll(possibleOutcomes.possibleRobots());
        possibleResources.addAll(possibleOutcomes.possibleResources());
    }
}

public class Advent19 {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String fileName = "input/input19.txt";
        int phase1Answer = 0;
        int phase2Answer = 1;
        BufferedReader reader;
        Map<Integer, RobotRequirements[]> blueprints = new HashMap<>();
        ProgressBarBuilder pbb = new ProgressBarBuilder()
                .setStyle(ProgressBarStyle.COLORFUL_UNICODE_BAR)
                .setTaskName("Running Blueprints ")
                .setUnit(" Blueprints", 1)
                .showSpeed()
                .setMaxRenderedLength(100);
        try {
            reader = new BufferedReader(new java.io.FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                String[] parts = line.split(" ");
                int blueprint = Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
                int index = 2;
                RobotRequirements[] robotsRequirement = new RobotRequirements[4];
                int robotIndex = 0;
                while (robotIndex < 4) {
                    index += 4;
                    Map<Integer, Integer> requires = new HashMap<>();
                    requires.put(RobotRequirements.MATERIAL.get(parts[index+1].replaceAll("[^a-z]", "")), Integer.parseInt(parts[index].replaceAll("[^0-9]", "")));
                    index++;
                    if (parts.length > index + 1 && parts[index + 1].equals("and")) {
                        index += 2;
                        requires.put(RobotRequirements.MATERIAL.get(parts[index+1].replaceAll("[^a-z]", "")), Integer.parseInt(parts[index].replaceAll("[^0-9]", "")));
                        index++;
                    }
                    robotsRequirement[robotIndex] = new RobotRequirements(robotIndex++, requires);
                    if(parts.length >= index) {
                        index++;
                    }
                }
                blueprints.put(blueprint, robotsRequirement);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int b : ProgressBar.wrap(blueprints.keySet(), pbb)) {
            RobotRequirements[] robotRequirements = blueprints.get(b);
            Robots robots = new Robots(new int[RobotRequirements.MATERIAL.size()]);
            robots.add(RobotRequirements.MATERIAL.get(RobotRequirements.ORE));
            int ore = getOre(robotRequirements, robots, new Resources(), 24, new HashMap<>());
            phase1Answer += ore * b;
        }
        System.out.println();
        System.out.println("Phase 1 Answer: " + phase1Answer);
        for(int b : ProgressBar.wrap(blueprints.keySet().stream().limit(3).collect(Collectors.toSet()), pbb)) {
            RobotRequirements[] robotRequirements = blueprints.get(b);
            Robots robots = new Robots(new int[RobotRequirements.MATERIAL.size()]);
            robots.add(RobotRequirements.MATERIAL.get(RobotRequirements.ORE));
            int ore = getOre(robotRequirements, robots, new Resources(), 32, new HashMap<>());
            phase2Answer *= ore;
        }
        System.out.println();
        System.out.println("Phase 2 Answer: " + phase2Answer);
        long currentTime = System.currentTimeMillis();
        double elapsedTime = (currentTime - startTime) / 1000.0;
        System.out.println("Time in seconds : " + elapsedTime);
    }
    private static int getOre(RobotRequirements[] robots, Robots currentRobots, Resources currentResources, int minute, Map<String, Integer> previousCalculations) {
        String previousCalculation = currentRobots.hashCode() + ":" + currentResources.hashCode() + ":" + minute;
        if (previousCalculations.containsKey(previousCalculation)) {
            return previousCalculations.get(previousCalculation);
        }
        if (minute == 0) {
            return currentResources.get(RobotRequirements.MATERIAL.get(RobotRequirements.GEODE));
        }
        int retVal = 0;
        PossibleOutcomes possibleOutcomes = new PossibleOutcomes(new ArrayList<>(), new ArrayList<>());
        if (currentResources.canMake(robots[RobotRequirements.MATERIAL.get(RobotRequirements.GEODE)])) {
            possibleOutcomes.AddPossibleOutcomes(buildRobotIfPossible(robots, currentRobots, currentResources,minute, RobotRequirements.GEODE));
        } else {
            possibleOutcomes.AddPossibleOutcomes(buildRobotIfPossible(robots, currentRobots, currentResources,minute, RobotRequirements.OBSIDIAN));
            possibleOutcomes.AddPossibleOutcomes(buildRobotIfPossible(robots, currentRobots, currentResources,minute, RobotRequirements.CLAY));
            if (currentResources.canMake(robots[RobotRequirements.MATERIAL.get(RobotRequirements.ORE)])) {
                int max = Math.max(Math.max(robots[RobotRequirements.MATERIAL.get(RobotRequirements.ORE)]
                        .getRequires(RobotRequirements.MATERIAL.get(RobotRequirements.ORE)), robots[RobotRequirements.MATERIAL.get(RobotRequirements.CLAY)]
                        .getRequires(RobotRequirements.MATERIAL.get(RobotRequirements.ORE))), Math.max(robots[RobotRequirements.MATERIAL
                        .get(RobotRequirements.OBSIDIAN)].getRequires(RobotRequirements.MATERIAL.get(RobotRequirements.ORE)),robots[RobotRequirements.MATERIAL
                        .get(RobotRequirements.GEODE)].getRequires(RobotRequirements.MATERIAL.get(RobotRequirements.ORE))));
                if(!(currentResources.get(RobotRequirements.MATERIAL.get(RobotRequirements.ORE)) > minute * (max - currentRobots
                        .get(RobotRequirements.MATERIAL.get(RobotRequirements.ORE))))) {
                    possibleOutcomes.AddPossibleOutcomes(buildRobotIfPossible(robots, currentRobots, currentResources,minute, RobotRequirements.ORE));
                }
            }
            possibleOutcomes.addPossibleResources(currentResources);
            possibleOutcomes.addPossibleRobots(currentRobots);
        }
        for (int i = 0; i < possibleOutcomes.possibleRobots().size(); i++) {
            Resources newResources = new Resources();
            for (int j = 0; j < RobotRequirements.MATERIAL.size(); j++) {
                newResources.add(j, currentRobots.get(j) + possibleOutcomes.possibleResources().get(i).get(j));
            }
            retVal = Math.max(retVal, getOre(robots, possibleOutcomes.possibleRobots().get(i), newResources, minute - 1, previousCalculations));
        }
        previousCalculations.put(previousCalculation, retVal);
        return retVal;
    }
    private static PossibleOutcomes buildRobotIfPossible(RobotRequirements[] robots, Robots currentRobots, Resources currentResources, int minute, String element) {
        PossibleOutcomes possibleOutcomes = new PossibleOutcomes(new ArrayList<>(), new ArrayList<>());
        if (currentResources.canMake(robots[RobotRequirements.MATERIAL.get(element)])) {
            if(element.equalsIgnoreCase(RobotRequirements.GEODE) || element.equalsIgnoreCase(RobotRequirements.ORE) || !(currentResources.get(RobotRequirements.MATERIAL.get(element)) > minute *
                    (robots[RobotRequirements.MATERIAL.get(element)+1]
                            .getRequires(RobotRequirements.MATERIAL.get(element)) - currentRobots
                            .get(RobotRequirements.MATERIAL.get(element))))) {
                Robots newRobots = new Robots(currentRobots.robots().clone());
                Resources newResources = new Resources(currentResources.resources.clone());
                newRobots.add(newResources.make(robots[RobotRequirements.MATERIAL.get(element)]));
                possibleOutcomes.addPossibleRobots(newRobots);
                possibleOutcomes.addPossibleResources(newResources);
            }
        }
        return possibleOutcomes;
    }
}
