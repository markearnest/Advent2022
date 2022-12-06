import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class Advent06 {
    private static int findMarker(String message, int numUnique) {
        for(int x = 0;x < message.length() - numUnique;x++) {
            String check = message.substring(x, x+numUnique);
            boolean isMarker = true;
            for(char c : check.toCharArray()) {
                if(check.chars().filter(ch -> ch == c).count() > 1) {
                    isMarker = false;
                }
            }
            if(isMarker) {
                return x + numUnique;
            }
        }
        return 0;
    }
    public static void main(String[] args) {
        String fileName = "06/input.txt";
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            System.out.println("first marker at position: " + findMarker(line, 4));
            System.out.println("first message at position: " + findMarker(line, 14));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
