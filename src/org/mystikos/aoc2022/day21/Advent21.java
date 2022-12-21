package org.mystikos.aoc2022.day21;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

class Monkey {
    public long getNumber() {
        return number;
    }
    public void setNumber(long number) {
        this.number = number;
    }
    public String getJob() {
        return job;
    }
    public boolean isDone() {
        return done;
    }
    public void setDone(boolean done) {
        this.done = done;
    }
    private long number;
    private final String job;
    private boolean done;
    public Monkey(String job) {
        this.job = job;
        this.number = -1;
        this.done = false;
    }
    public Monkey(long number) {
        this.number = number;
        this.job = null;
        this.done = true;
    }
    public String[] getJobMonkeys() {
        if(job == null) {
            return null;
        }
        return new String[] {job.split(" ")[0], job.split(" ")[2]};
    }
}

public class Advent21 {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String fileName = "input/input21.txt";
        long phase1Answer;
        long phase2Answer = 0;
        BufferedReader reader;
        Map<String, Monkey> barrelOfMonkeys = new HashMap<>();
        try {
            reader = new BufferedReader(new java.io.FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                String[] splitLine = line.split(":");
                if(isInteger(splitLine[1].trim())) {
                    barrelOfMonkeys.put(splitLine[0].trim(), new Monkey(Long.parseLong(splitLine[1].trim())));
                } else {
                    barrelOfMonkeys.put(splitLine[0].trim(), new Monkey(splitLine[1].trim()));
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Monkey root = barrelOfMonkeys.get("root");
        phase1Answer = runMonkeyJob(root, barrelOfMonkeys, false);
        System.out.println("Phase 1 Answer: " + phase1Answer);

        /* Used these two calls to zero in close enough to the number to be able to brute force it
          Spent about 20 minutes getting down to this upper and lower bound, then ran it and it found the number
          in 0.5 seconds. I'm not particularly proud of this solution, but it works. Someday I'll come back and
          write an algorithm to do this properly.
         */
        long i;
        i = 3882224465655L;
        barrelOfMonkeys.get("humn").setNumber(i);
        if(checkMonkeyEquality(root, barrelOfMonkeys, true)) {
            phase2Answer = i;
        }
        i = 3882224555555L;
        barrelOfMonkeys.get("humn").setNumber(i);
        if(checkMonkeyEquality(root, barrelOfMonkeys, true)) {
            phase2Answer = i;
        }
        for(i = 3882224465655L;i < 3882224555555L; i++) {
            barrelOfMonkeys.get("humn").setNumber(i);
            if(checkMonkeyEquality(root, barrelOfMonkeys, false)) {
                phase2Answer = i;
                break;
            }
        }
        System.out.println("Phase 2 Answer: " + phase2Answer);
        long currentTime = System.currentTimeMillis();
        double elapsedTime = (currentTime - startTime) / 1000.0;
        System.out.println("Time in seconds : " + elapsedTime);
    }
    public static boolean checkMonkeyEquality(Monkey monkey, Map<String, Monkey> barrelOfMonkeys, boolean print) {
        if (print) System.out.println("Checking equality for monkey: " + (runMonkeyJob(barrelOfMonkeys.get(monkey.getJobMonkeys()[0]), barrelOfMonkeys, true) - runMonkeyJob(barrelOfMonkeys.get(monkey.getJobMonkeys()[1]), barrelOfMonkeys, true)));
        return runMonkeyJob(barrelOfMonkeys.get(monkey.getJobMonkeys()[0]), barrelOfMonkeys, true) == runMonkeyJob(barrelOfMonkeys.get(monkey.getJobMonkeys()[1]), barrelOfMonkeys, true);
    }
    public static long runMonkeyJob(Monkey monkey, Map<String, Monkey> barrelOfMonkeys, boolean recalculate) {
        if(!recalculate && monkey.isDone()) {
            return monkey.getNumber();
        } else if(recalculate && monkey.getJobMonkeys() == null) {
            return monkey.getNumber();
        }
        long m1 = runMonkeyJob(barrelOfMonkeys.get(monkey.getJobMonkeys()[0]), barrelOfMonkeys, recalculate);
        long m2 = runMonkeyJob(barrelOfMonkeys.get(monkey.getJobMonkeys()[1]), barrelOfMonkeys, recalculate);
        long result = switch (monkey.getJob().split(" ")[1]) {
            case "+" -> m1 + m2;
            case "-" -> m1 - m2;
            case "*" -> m1 * m2;
            case "/" -> m1 / m2;
            default -> 0;
        };
        monkey.setNumber(result);
        monkey.setDone(true);
        return result;
    }
    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }
    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }
}
