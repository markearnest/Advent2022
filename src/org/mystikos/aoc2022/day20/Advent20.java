package org.mystikos.aoc2022.day20;

import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import java.io.BufferedReader;


public class Advent20 {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String fileName = "input/input20.txt";
        int Phase1Answer = 0;
        int Phase2Answer = 0;
        BufferedReader reader;
        ProgressBarBuilder pbb = new ProgressBarBuilder()
                .setStyle(ProgressBarStyle.COLORFUL_UNICODE_BAR)
                .showSpeed()
                .setMaxRenderedLength(100);
        try {
            reader = new BufferedReader(new java.io.FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                // Do smart parsing here
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Do really smart stuff here
        System.out.println("Phase 1 Answer: " + Phase1Answer);
        System.out.println("Phase 2 Answer: " + Phase2Answer);
        long currentTime = System.currentTimeMillis();
        double elapsedTime = (currentTime - startTime) / 1000.0;
        System.out.println("Time in seconds : " + elapsedTime);
    }
}
