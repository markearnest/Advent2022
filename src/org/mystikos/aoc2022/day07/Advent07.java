package org.mystikos.aoc2022.day07;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.Integer;

class Directory {
    private String name;
    private int containedFileSize;
    private final List<Directory> children;
    public Directory() {
        children = new ArrayList<>();
        containedFileSize = 0;
    }
    public Directory(String name) {
        this();
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void addChild(Directory d) {
        children.add(d);
    }
    public List<Directory> getChildren() {
        return children;
    }
    public void addFileSize(int size) {
        containedFileSize += size;
    }
    public int getSize() {
        int childSize = 0;
        for(Directory d : children) {
            childSize += d.getSize();
        }
        return childSize + containedFileSize;
    }
}

public class Advent07 {
    private static int total = 0;
    private static String idealDirName = "";
    private static int idealDirSize = Integer.MAX_VALUE;

    public static void main(String[] args) {
        String fileName = "input/input07.txt";
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            Directory root = new Directory("/");
            processCommands(reader, reader.readLine(), root);
            findTotalUnder(root, 100000);
            findIdealDirToDelete(root, 30000000 - (70000000 - root.getSize()));
            System.out.println("Total = " + total);
            System.out.println("Ideal directory to delete = " + idealDirName + " : " + idealDirSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void findTotalUnder(Directory d, int underSize) {
        if(d.getSize() < underSize) {
            total += d.getSize();
        }
        for(Directory child : d.getChildren()) {
            findTotalUnder(child, underSize);
        }
    }

    private static void findIdealDirToDelete(Directory d, int freeSpaceNeeded) {
        int size = d.getSize();
        if(size > freeSpaceNeeded && size < idealDirSize) {
            idealDirSize = size;
            idealDirName = d.getName();
        }
        for(Directory child : d.getChildren()) {
            findIdealDirToDelete(child, freeSpaceNeeded);
        }
    }

    private static int processCommands(BufferedReader reader, String line, Directory d) throws IOException {
        if(line == null) {
            return -1; // we are done
        }
        while (line != null) {
            if (line.charAt(0) == '$') {
                if (line.substring(2).equalsIgnoreCase("ls")) { // list contents of directory
                    line = reader.readLine();
                    while (line != null && line.charAt(0) != '$') {
                        if (line.split(" ")[0].equalsIgnoreCase("dir")) {
                            d.addChild(new Directory(line.split(" ")[1]));
                        } else {
                            d.addFileSize(Integer.parseInt(line.split(" ")[0]));
                        }
                        line = reader.readLine();
                    }
                }
                if (line == null) {
                    return -1; // we are done
                }
                if (line.substring(2).split(" ")[0].equalsIgnoreCase("cd")) { // change directory
                    if (line.substring(2).split(" ")[1].equalsIgnoreCase("..")) {
                        return 0; // go up a level and keep going
                    } else {
                        for (Directory child : d.getChildren()) {
                            if (child.getName().equalsIgnoreCase(line.substring(2).split(" ")[1])) {
                                if (processCommands(reader, reader.readLine(), child) == -1) {
                                    return -1;
                                }
                            }
                        }
                    }
                }
            }
            line = reader.readLine();
        }
        return 0;
    }
}
