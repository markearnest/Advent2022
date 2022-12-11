import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.lang.Integer;
import java.lang.Long;

class Monkey {
    private Queue<BigInteger> items;
    private String op;
    private int test;
    private int throwTrue;
    private int throwFalse;
    private long views;
    public Monkey() {
        items = new LinkedList<BigInteger>();
        op = new String();
        test = 0;
        throwTrue = 0;
        throwFalse = 0;
        views = 0;
    }

    public void addItem(BigInteger i) {
        items.add(i);
    }

    public long getViews() {
        return this.views;
    }

    public Queue<BigInteger> getItems() {
        return items;
    }

    public BigInteger inspectItem() {
        views++;
        return items.poll();
    }

    public boolean hasItem() {
        return !items.isEmpty();
    }

    private String getOp() {
        return op;
    }
    public BigInteger processWorry(BigInteger i) {
        if(this.getOp().split(" ")[0].trim().equalsIgnoreCase("*")) {
            if(this.getOp().split(" ")[1].trim().equalsIgnoreCase("old")) {
                return i.multiply(i);
            } else {
                return i.multiply(BigInteger.valueOf(Long.parseLong(this.getOp().split(" ")[1].trim())));
            }
        }
        return i.add(BigInteger.valueOf(Long.parseLong(this.getOp().split(" ")[1].trim())));
    }

    public BigInteger getWorryLevel(BigInteger i, int intDivideBy) {
        return i;
    }

    public boolean test(BigInteger i) {
        return i.mod(BigInteger.valueOf(this.getTest()))==BigInteger.valueOf(0);
    }

    public int getMonkeyThrowTo(BigInteger i) {
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
        String fileName = "11/inputdemo.txt";
        BufferedReader reader;
        try {
            HashMap<Integer, Monkey> monkeyMap = new HashMap<Integer, Monkey>();
            reader = new BufferedReader(new FileReader(fileName));

            int rounds = 1000; // round 1: 20 - round 2: 10000
            int divideBy = 3; // round 1: 3 - round 2: 1
            String line = reader.readLine();
            while (line != null) {
                Monkey monkey = new Monkey();
                Integer monkeyIndex = Integer.parseInt(String.valueOf(line.split(" ")[1].trim().charAt(0)));
                for(String items : reader.readLine().substring(18).split(",")) {
                    monkey.addItem(BigInteger.valueOf(Long.parseLong(items.trim())));
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

            for(int x = 0; x < rounds ; x++) {
                if(x%100==0) {
                    System.out.println("Round "+x);
                }
                for(int m = 0; m < monkeyMap.size(); m++) {
                    Monkey monkey = monkeyMap.get(m);
                    while (monkey.hasItem()) {
                        BigInteger item = monkey.inspectItem();
                        //item = monkey.getWorryLevel(item, divideBy);
                        item = monkey.processWorry(item);
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
            Collections.sort(viewCount, Collections.reverseOrder());
            System.out.println("Answer: "+viewCount.get(0)*viewCount.get(1));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
