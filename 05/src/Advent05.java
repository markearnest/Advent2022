import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Character;
import java.util.Stack;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.lang.Integer;

class StackReadLine {
    final Map <Integer, Character> processedLine = new HashMap<>();
    StackReadLine(String str) {
        int position = 1;
        for(int x = 0; x <= str.length(); x += 4) {
            if (str.charAt(x) == '[') {
                processedLine.put(position, str.charAt(x + 1));
            }
            position++;
        }
    }

    public Map<Integer, Character> getProcessedLine() {
        return processedLine;
    }
}
class StackOperation {
    private int numberOfMoves;
    private Integer from;
    private Integer to;
    StackOperation(String line) {
        if(line.startsWith("move ")) {
            line = line.substring(line.indexOf(' ')+1);
            this.numberOfMoves = Integer.parseInt(line.substring(0, line.indexOf(' ')).trim());
            line = line.substring(line.indexOf("from ") + 5);
            this.from = Integer.valueOf(line.substring(0,line.indexOf(' ')).trim());
            line = line.substring(line.indexOf("to ") + 3);
            this.to = Integer.valueOf(line.trim());
        }
    }
    public int getNumberOfMoves() {
        return numberOfMoves;
    }
    public Integer getFrom() {
        return from;
    }
    public Integer getTo() {
        return to;
    }
}
public class Advent05 {
    public static void main(String[] args) {
        boolean oneCrateAtATime = false; // for round 2
        Map<Integer, Stack<Character>> stacks = new HashMap<>();
        String fileName = "05/input.txt";
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            Stack<StackReadLine> readLineStack = new Stack<>();
            int numberOfStacks = 0;
            while (line != null) {
                System.out.println("Parsing line: " + line);
                if (line.charAt(1) == '1') {
                    for (int x = 1; x <= line.length(); x += 4) {
                        numberOfStacks = Integer.parseInt(String.valueOf(line.charAt(x)));
                    }
                    line = reader.readLine(); // blank line after stacks
                    break;
                }
                readLineStack.add(new StackReadLine(line));
                line = reader.readLine();
            }
            int stackSize =  readLineStack.size();
            for (int x = 0 ; x < stackSize; x++) {
                Map <Integer, Character> processedLine = readLineStack.pop().getProcessedLine();
                for (Entry<Integer, Character> entry:  processedLine.entrySet()) {
                    if (!stacks.containsKey(entry.getKey())) {
                        stacks.put(entry.getKey(), new Stack<>());
                    }
                        stacks.get(entry.getKey()).add(entry.getValue());
                }
            }
            line = reader.readLine();
            while (line != null) {
                StackOperation so = new StackOperation(line);
                if (oneCrateAtATime) {
                    for (int x = 0; x < so.getNumberOfMoves(); x++) {
                        Character item = stacks.get(so.getFrom()).pop();
                        stacks.get(so.getTo()).add(item);
                    }
                } else {
                    Stack<Character> tempStack = new Stack<>();
                    for(int x = 0; x < so.getNumberOfMoves(); x++) {
                        tempStack.add(stacks.get(so.getFrom()).pop());
                    }
                    int tempStackSize = tempStack.size();
                    for(int x = 0; x < tempStackSize; x++) {
                        stacks.get(so.getTo()).add(tempStack.pop());
                    }
                }
                line = reader.readLine();
            }
            StringBuilder sb = new StringBuilder();
            for(int x = 1; x<=numberOfStacks; x++) {
                sb.append(stacks.get(x).peek());
            }
            System.out.println("Top rows: " + sb);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
