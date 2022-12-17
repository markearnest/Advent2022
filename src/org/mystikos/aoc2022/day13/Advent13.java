package org.mystikos.aoc2022.day13;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.lang.Character;

record Item(String packet) implements Comparable<Item> {
    public boolean equals(Item o) {
        return packet.equals(o.packet);
    }
    @Override
    public int compareTo(Item o) {
        if (this.equals(o)) return 0;
        if (Items.compare(this.packet, o.packet()) == 1) return 1;
        else return -1;
    }
}
class Items {
    private final String leftSide;
    private final String rightSide;
    public int getIndex() {
        return index;
    }
    private final int index;
    public String getLeftSide() {
        return leftSide;
    }
    public String getRightSide() {
        return rightSide;
    }
    public Items(String left, String right, int index) {
        this.leftSide = left;
        this.rightSide = right;
        this.index = index;
    }
     private static int findClosingBracket(String a) {
        int z = 1;
        for (int pos = 1; pos < a.length(); pos++) {
            char x = a.charAt(pos);
            if (x == '[') z += 1;
            else if (x == ']') z -= 1;
            if (z == 0) return pos;
        }
        return -1;
    }
    public int compare() {
        return compare(this.leftSide, this.rightSide);
    }
    private static boolean isNotNullOrEmpty(String s) {
        return (s!=null && !s.isEmpty());
    }
    public static int compare(String leftSide, String rightSide) {
        int retVal = -1;
        while(isNotNullOrEmpty(leftSide) && isNotNullOrEmpty(rightSide) && retVal == -1) {
            char firstLeftSide = leftSide.charAt(0);
            char firstRightSide = rightSide.charAt(0);
            String internalLeftSide = "";
            String internalRightSide = "";
            if(firstLeftSide=='[' || firstRightSide=='[') {
                int closingLeftSide = -1;
                int closingRightSide = -1;
                if(firstLeftSide=='[' && firstRightSide!='[') {
                    closingLeftSide = findClosingBracket(leftSide);
                    internalLeftSide = leftSide.substring(1, closingLeftSide);
                    internalRightSide = !rightSide.contains(",") ? rightSide : rightSide.substring(0,rightSide.indexOf(","));
                } else if (firstLeftSide!='[' && firstRightSide=='[') {
                    closingRightSide = findClosingBracket(rightSide);
                    internalLeftSide = !leftSide.contains(",") ? leftSide : leftSide.substring(0,leftSide.indexOf(","));
                    internalRightSide = rightSide.substring(1, closingRightSide);
                } else if (firstLeftSide=='[' && firstRightSide=='[') {
                    closingLeftSide = findClosingBracket(leftSide);
                    closingRightSide = findClosingBracket(rightSide);
                    internalLeftSide = leftSide.substring(1, closingLeftSide);
                    internalRightSide = rightSide.substring(1, closingRightSide);
                }
                retVal = compare(internalLeftSide, internalRightSide);
                if(retVal != -1) return retVal;
                leftSide = closingLeftSide > -1 ? leftSide.substring(closingLeftSide+1) : leftSide.length()>1 ? leftSide.substring(2): "";
                if(leftSide.length()>0) leftSide = leftSide.charAt(0)==',' ? leftSide.substring(1) : leftSide;
                rightSide = closingRightSide > -1 ? rightSide.substring(closingRightSide+1) : rightSide.length()>1 ? rightSide.substring(2) : "";
                if(rightSide.length()>0) rightSide = rightSide.charAt(0)==',' ? rightSide.substring(1) : rightSide;
            } else if (Character.isDigit(leftSide.charAt(0)) && Character.isDigit(rightSide.charAt(0))) {
                int numLeftSide = !leftSide.contains(",") ? Integer.parseInt(leftSide) :  Integer.parseInt(leftSide.substring(0,leftSide.indexOf(",")));
                int numRightSide = !rightSide.contains(",") ? Integer.parseInt(rightSide) :  Integer.parseInt(rightSide.substring(0,rightSide.indexOf(",")));
                if(numLeftSide > numRightSide) return 0;
                else if (numLeftSide < numRightSide) return 1;
                leftSide = leftSide.indexOf(',') == -1 ? "" : leftSide.substring(leftSide.indexOf(',')+1);
                rightSide = rightSide.indexOf(',') == -1 ? "" : rightSide.substring(rightSide.indexOf(',')+1);
            }
        }
        if(!isNotNullOrEmpty(leftSide) && isNotNullOrEmpty(rightSide)) return 1;
        if(isNotNullOrEmpty(leftSide) && !isNotNullOrEmpty(rightSide)) return 0;
        return retVal;
    }
}
public class Advent13 {
    public static void main(String[] args) {
        String fileName = "input/input13.txt";
        BufferedReader reader;
        ArrayList<Items> items = new ArrayList<>();
        ArrayList<Item> sortedItems = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            int index = 0;
            while (line != null) {
                String leftSide = line;
                String rightSide = reader.readLine();
                line = reader.readLine();
                line = reader.readLine();
                items.add(new Items(leftSide, rightSide, ++index));
            }
            int phase1 = 0;
            for(Items item : items) {
                if(item.compare() == 1) phase1 += item.getIndex();
                sortedItems.add(new Item(item.getLeftSide()));
                sortedItems.add(new Item(item.getRightSide()));
            }
            Item marker1 = new Item("[[2]]");
            Item marker2 = new Item("[[6]]");
            sortedItems.add(marker1);
            sortedItems.add(marker2);
            sortedItems.sort(Collections.reverseOrder());
            System.out.println("Phase 1: "+phase1);
            System.out.println("Phase 2: "+(sortedItems.indexOf(marker1)+1)*(sortedItems.indexOf(marker2)+1));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
