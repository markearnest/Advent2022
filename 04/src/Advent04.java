import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.Integer;

record Team(int lower, int upper) {

    public boolean encompass(Team otherTeam) {
        return this.lower <= otherTeam.lower() && this.upper >= otherTeam.upper();
    }

    public boolean overlap(Team otherTeam) {
        if (this.encompass(otherTeam)) {
            return true;
        }
        if (this.lower <= otherTeam.lower() && this.upper >= otherTeam.lower()) {
            return true;
        }
        return this.upper >= otherTeam.upper() && this.lower <= otherTeam.upper();
    }
}

record TeamPairs(Team team1, Team team2) {

    public boolean encompass() {
        return this.team1.encompass(this.team2) || this.team2.encompass(this.team1);
    }

    public boolean overlap() {
        return this.team1.overlap(this.team2) || this.team2.overlap(this.team1);
    }
}
public class Advent04 {
    public static void main(String[] args) {
        List<TeamPairs> teamPairList = new ArrayList<>();
        String fileName = "04/input.txt";
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