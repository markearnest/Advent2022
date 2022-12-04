import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.lang.Integer;
import java.lang.CharSequence;
import java.lang.StringBuilder;
import java.lang.Character;

class RuckSack {
    private String compartment1;
    private String compartment2;

    public RuckSack() {

    }
    public RuckSack(String contents) {
        final int mid = contents.length() / 2;
        String[] parts = {contents.substring(0, mid),contents.substring(mid)};
        this.compartment1 = parts[0];
        this.compartment2 = parts[1];
    }

    public char getDupe() {
        for(char c : this.compartment1.toCharArray()) {
            if(this.compartment2.contains(String.valueOf(c))) {
                return c;
            }
        }
        return '0';
    }

    public String getDupe(String str) {
        StringBuilder sb = new StringBuilder();
        for(char c : this.getContents().toCharArray()) {
            if(str.contains(String.valueOf(c))) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public String getCompartment1() {
        return compartment1;
    }

    public String getCompartment2() {
        return compartment2;
    }

    public String getContents() {
        return compartment1.concat(compartment2);
    }
}

public class Main {

    public static void main(String[] args) {
        List<RuckSack> rsList = new ArrayList<RuckSack>();
        String fileName = "/home/mark/src/adventcode/03/input.txt";
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                rsList.add(new RuckSack(line));
                line = reader.readLine();
            }
            int total = 0;
            for(RuckSack rs : rsList) {
                //System.out.println(rs.getCompartment1()+":"+rs.getCompartment2()+" Dupe="+rs.getDupe()+":"+getPriority(rs.getDupe()));
                total += getPriority(rs.getDupe());
            }
            System.out.println("total = " + total);
            int total2 = 0;
            for(int x = 0; x < rsList.size(); x += 3) {
                String common = rsList.get(x).getDupe(rsList.get(x+1).getContents());
                common = rsList.get(x+2).getDupe(common);
                total2 += getPriority(Character.valueOf(common.charAt(0)));
            }
            System.out.println("total2 = " + total2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getPriority(char c) {
        int retVal = 0;
        if(Character.isUpperCase(c)) {
            retVal = ((int)c)-64+26;
        } else {
            retVal = ((int)c)-96;
        }
        return retVal;
    }

}