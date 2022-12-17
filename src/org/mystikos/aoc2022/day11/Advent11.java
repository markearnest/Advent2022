package org.mystikos.aoc2022.day11;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.lang.Integer;
import java.lang.Long;

class Monkey {
    private final Queue<Long> items;
    private String op;
    private int test;
    private int throwTrue;
    private int throwFalse;
    private long views;
    public Monkey() {
        items = new LinkedList<>();
        op = "";
        test = 0;
        throwTrue = 0;
        throwFalse = 0;
        views = 0;
    }

    public void addItem(Long i) {
        items.add(i);
    }

    public long getViews() {
        return this.views;
    }

    public Long inspectItem() {
        views++;
        return items.poll();
    }

    public boolean hasItem() {
        return !items.isEmpty();
    }

    private String getOp() {
        return op;
    }
    public Long processWorry(Long i, int lcm) {
        long retVal;
        if(this.getOp().split(" ")[0].trim().equalsIgnoreCase("*")) {
            if(this.getOp().split(" ")[1].trim().equalsIgnoreCase("old")) {
                retVal = i*i;
            } else {
                retVal = i * Integer.parseInt(this.getOp().split(" ")[1].trim());
            }
        } else {
            retVal = i + Integer.parseInt(this.getOp().split(" ")[1].trim());
        }
        return retVal%lcm;
    }

    public Long getWorryLevel(Long i, int lcm, int divideBy) {
        return processWorry(i, lcm)/divideBy;
    }

    public boolean test(Long i) {
        return i%this.getTest()==0;
    }

    public int getMonkeyThrowTo(Long i) {
        if(this.test(i)) {
            return this.getThrowTrue();
        }
        return this.getThrowFalse();
    }

    public void setOp(String op) {
        this.op = op;
    }

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }

    public int getThrowTrue() {
        return throwTrue;
    }

    public void setThrowTrue(int throwTrue) {
        this.throwTrue = throwTrue;
    }

    public int getThrowFalse() {
        return throwFalse;
    }

    public void setThrowFalse(int throwFalse) {
        this.throwFalse = throwFalse;
    }
}
public class Advent11 {
    public static void main(String[] args) {
        String fileName = "input/input11.txt";
        BufferedReader reader;
        try {
            HashMap<Integer, Monkey> monkeyMap = new HashMap<>();
            reader = new BufferedReader(new FileReader(fileName));

            int rounds = 10000; // round 1: 20 - round 2: 10000
            int divideBy = 1; // round 1: 3 - round 2: 1
            String line = reader.readLine();
            while (line != null) {
                Monkey monkey = new Monkey();
                Integer monkeyIndex = Integer.parseInt(String.valueOf(line.split(" ")[1].trim().charAt(0)));
                for(String items : reader.readLine().substring(18).split(",")) {
                    monkey.addItem(Long.parseLong(items.trim()));
                }
                monkey.setOp(reader.readLine().substring(23));
                monkey.setTest(Integer.parseInt(reader.readLine().substring(21)));
                monkey.setThrowTrue(Integer.parseInt(reader.readLine().split("monkey")[1].trim()));
                monkey.setThrowFalse(Integer.parseInt(reader.readLine().split("monkey")[1].trim()));
                monkeyMap.put(monkeyIndex, monkey);
                for (int i = 0; i < 2; i++) {
                    line = reader.readLine();
                }
            }
            ArrayList<Integer> tests = new ArrayList<>();
            for(int m = 0; m < monkeyMap.size(); m++) {
                tests.add(monkeyMap.get(m).getTest());
            }
            int lcm = lowestCommonDenominator(tests, 0);
            System.out.println(lcm);

            for(int x = 0; x < rounds ; x++) {
                for(int m = 0; m < monkeyMap.size(); m++) {
                    Monkey monkey = monkeyMap.get(m);
                    while (monkey.hasItem()) {
                        Long item = monkey.inspectItem();
                        item = monkey.getWorryLevel(item,lcm,divideBy);
                        int monkeyThrowTo = monkey.getMonkeyThrowTo(item);
                        monkeyMap.get(monkeyThrowTo).addItem(item);
                    }
                }
            }

            ArrayList<Long> viewCount = new ArrayList<>();
            for(int m = 0; m < monkeyMap.size(); m++) {
                viewCount.add(monkeyMap.get(m).getViews());
                System.out.println("Monkey "+m+": "+monkeyMap.get(m).getViews());
            }
            viewCount.sort(Collections.reverseOrder());
            System.out.println("Answer: "+viewCount.get(0)*viewCount.get(1));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int lowestCommonDenominator(List<Integer> testNumbers, int i) {
        if(i == testNumbers.size() - 1) {
            return testNumbers.get(i);
        }
        int a = testNumbers.get(i);
        int b = lowestCommonDenominator(testNumbers, i + 1);
        return (a * b) / greatestCommonDenominator(a, b);
    }
    public static int greatestCommonDenominator(int a, int b) {
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return a;
    }
}
