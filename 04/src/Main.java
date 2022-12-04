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

class Team {
    private int lower;
    private int upper;
    public Team() {

    }
    public Team(int lower, int upper) {
        this.upper = upper;
        this.lower = lower;
    }
    public void setLower(int lower) {
        this.lower = lower;
    }
    public void setUpper(int upper) {
        this.upper = upper;
    }
    public int getLower() {
        return lower;
    }
    public int getUpper() {
        return upper;
    }

    public boolean encompass(Team otherTeam) {
        return this.lower <= otherTeam.getLower() && this.upper >= otherTeam.getUpper();
    }

    public boolean overlap(Team otherTeam) {
        if(this.encompass(otherTeam)) {
            return true;
        }
        if(this.lower <= otherTeam.getLower() && this.upper >= otherTeam.getLower()) {
            return true;
        }
        if(this.upper >= otherTeam.getUpper() && this.lower <= otherTeam.getUpper()) {
            return true;
        }
        return false;
    }
}
class TeamPairs {
    Team team1;
    Team team2;
    public TeamPairs(Team team1, Team team2) {
        this.team1 = team1;
        this.team2 = team2;
    }

    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public boolean encompass() {
        return this.team1.encompass(this.team2) || this.team2.encompass(this.team1);
    }

    public boolean overlap() {
        return this.team1.overlap(this.team2) || this.team2.overlap(this.team1);
    }
}
public class Main {
    public static void main(String[] args) {
        List<TeamPairs> teamPairList = new ArrayList<>();
        String fileName = "/home/mark/src/adventcode/04/input.txt";
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();

            while (line != null) {
                Team team1 = new Team(Integer.parseInt(line.split(",")[0].split("-")[0]), Integer.parseInt(line.split(",")[0].split("-")[1]));
                Team team2 = new Team(Integer.parseInt(line.split(",")[1].split("-")[0]), Integer.parseInt(line.split(",")[1].split("-")[1]));
                teamPairList.add(new TeamPairs(team1, team2));
                line = reader.readLine();
            }
            int encompass = 0;
            int overlap = 0;
            for(TeamPairs tp : teamPairList) {
                if(tp.encompass()) {
                    encompass++;
                }
                if(tp.overlap()) {
                    overlap++;
                }
            }
            System.out.println("Total encompass = " + encompass);
            System.out.println("Total overlap = " + overlap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}