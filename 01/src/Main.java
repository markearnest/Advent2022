import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.lang.Integer;

public class Main {
    public static void main(String[] args) {
         String fileName = "/home/mark/src/adventcode/01/input.txt";
        List<Integer> calorieList = new ArrayList<Integer>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            int calorieCount = 0;
            while (line != null) {
                if(!line.equals("")) {
                    calorieCount += Integer.parseInt(line);
                } else {
                    calorieList.add(calorieCount);
                    calorieCount = 0;
                }
                line = reader.readLine();
            }
            reader.close();
            Collections.sort(calorieList, Collections.reverseOrder());
            int topNum = 3;
            int topVal = 0;
            for (int i = 0; i < topNum; i++) {
                topVal += calorieList.get(i).intValue();
            }
            System.out.println(topVal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}