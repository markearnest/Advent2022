import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.lang.Integer;

class Cycles {
    private int cycle;
    private int register;
    public Cycles() {
        cycle = 1;
        register = 1;
    }
    public int noop() {
        cycle++;
        if(checkSignalStrength()) {
            return signalStrength();
        } else {
            return 0;
        }
    }
    public int addX(int value) {
        cycle++;
        register += value;
        if(checkSignalStrength()) {
            return signalStrength();
        } else {
            return 0;
        }
    }
    private boolean checkSignalStrength() {
        if(cycle == 20) return true;
        if(cycle >=40) {
            return (cycle - 20) % 40 == 0;
        }
        return false;
    }
    private int signalStrength() {
        return cycle * register;
    }
    public int getRegister() {
        return register;
    }
}
class DrawLine {
    StringBuilder line;
    public DrawLine() {
        line = new StringBuilder();
    }
    public boolean addPixel(int register) {
        int pos = line.length();
        if (pos >= register - 1 && pos <= register +1) {
            line.append("#");
        } else {
            line.append(".");
        }
        return isEndOfLine();
    }
    public String getLine() {
        String retVal = line.toString();
        line = new StringBuilder();
        return retVal;
    }
    private boolean isEndOfLine() {
        return line.length() == 40;
    }
}
public class Advent10 {
    public static void main(String[] args) {
        String fileName = "10/input.txt";
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            Cycles cycles = new Cycles();
            ArrayList<String> screenLine = new ArrayList<>();
            DrawLine drawLine = new DrawLine();
            int signalStrength = 0;
            while (line != null) {
                int n,s = 0;
                if(line.equalsIgnoreCase("noop")) {
                    if(drawLine.addPixel(cycles.getRegister())) {
                        screenLine.add(drawLine.getLine());
                    }
                    n = cycles.noop();
                } else {
                    if(drawLine.addPixel(cycles.getRegister())) {
                        screenLine.add(drawLine.getLine());
                    }
                    n = cycles.noop();
                    if(drawLine.addPixel(cycles.getRegister())) {
                        screenLine.add(drawLine.getLine());
                    }
                    s = cycles.addX(Integer.parseInt(line.split(" ")[1]));
                }
                signalStrength += n+s;
                line = reader.readLine();
            }
            System.out.println("Signal Strength = " + signalStrength);
            for(String row : screenLine) {
                System.out.println(row);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
