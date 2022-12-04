import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.lang.Integer;
import java.lang.Character;
import java.util.Objects;

class Play {
    private final Map<Character, Integer> score = new HashMap<>();
    private Character theirs;
    private Character mine;
    public Play() {
        score.put('A', 1);
        score.put('B', 2);
        score.put('C', 3);
        score.put('X', 1);
        score.put('Y', 2);
        score.put('Z', 3);
    }

    public boolean tie ()  {
        return Objects.equals(score.get(this.theirs), score.get(this.mine));
    }

    public boolean win () {
        if(score.get(this.mine) == 1) {
            return score.get(this.theirs) == 3;
        }
        if(score.get(this.theirs) == 1) {
            return score.get(this.mine) != 3;
        }
        return score.get(this.mine) > score.get(this.theirs);
    }

    public Integer getScore () {
        if(this.tie()) {
            return 3 + score.get(this.mine);
        } else if (this.win()) {
            return 6 + score.get(this.mine);
        } else {
            return score.get(this.mine);
        }
    }

    public void mustTie() {
        this.mine = this.theirs;
    }

    public void mustWin() {
        if(score.get(this.theirs).equals(3)) {
            this.mine = 'A';
        } else if (score.get(this.theirs).equals(1)) {
            this.mine = 'B';
        } else  {
            this.mine = 'C';
        }
    }

    public void mustLose() {
        if(score.get(this.theirs).equals(3)) {
            this.mine = 'B';
        } else if (score.get(this.theirs).equals(1)) {
            this.mine = 'C';
        } else  {
            this.mine = 'A';
        }
    }

    public Character getTheirs() {
        return this.theirs;
    }

    public Character getMine() {
        return this.mine;
    }

    public void setTheirs(Character theirs) {
        this.theirs = theirs;
    }
}


public class Advent02 {
    public static void main(String[] args) {
        String fileName = "02/input.txt";
        Map<Character, String> name = new HashMap<>();
        name.put('A', "rock");
        name.put('X', "lose");
        name.put('B', "paper");
        name.put('Y', "tie");
        name.put('C', "scissors");
        name.put('Z', "win");

        int score = 0;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null && line.length() == 3) {
                Play play = new Play();
                play.setTheirs(line.charAt(0));
                if(name.get(line.charAt(2)).equalsIgnoreCase("tie")) {
                    play.mustTie();
                } else if (name.get(line.charAt(2)).equalsIgnoreCase("lose")) {
                    play.mustLose();
                } else if (name.get(line.charAt(2)).equalsIgnoreCase("win")) {
                    play.mustWin();
                }
                score += play.getScore();
                if(play.tie()) {
                    System.out.println(line+": "+name.get(play.getMine()) + " ties with "+name.get(play.getTheirs())+ " score = "+play.getScore());
                } else if (play.win()) {
                    System.out.println(line+": "+name.get(play.getMine()) + " beats "+name.get(play.getTheirs())+ " score = "+play.getScore());
                } else {
                    System.out.println(line+": "+name.get(play.getMine()) + " loses to "+name.get(play.getTheirs())+ " score = "+play.getScore());
                }

                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Score = "+score);

    }
}