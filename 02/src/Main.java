import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.lang.Integer;
import java.lang.Character;
class Play {
    private final Map<Character, Integer> score = new HashMap<Character, Integer>();
    private Character theirs;
    private Character mine;
    public Play() {
        score.put(Character.valueOf('A'), 1);
        score.put(Character.valueOf('B'), 2);
        score.put(Character.valueOf('C'), 3);
        score.put(Character.valueOf('X'), 1);
        score.put(Character.valueOf('Y'), 2);
        score.put(Character.valueOf('Z'), 3);
    }
    public Play(char theirs, char mine) {
        this();
        this.theirs = Character.valueOf(theirs);
        this.mine = Character.valueOf(mine);
    }

    public boolean tie ()  {
        return score.get(this.theirs) == score.get(this.mine);
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
            return  Integer.valueOf(3 + score.get(this.mine));
        } else if (this.win()) {
            return Integer.valueOf(6 + score.get(this.mine));
        } else {
            return score.get(this.mine);
        }
    }

    public void mustTie() {
        this.mine = this.theirs;
    }

    public void mustWin() {
        if(score.get(this.theirs).equals(Integer.valueOf(3))) {
            this.mine = Character.valueOf('A');
        } else if (score.get(this.theirs).equals(Integer.valueOf(1))) {
            this.mine = Character.valueOf('B');
        } else  {
            this.mine = Character.valueOf('C');
        }
    }

    public void mustLose() {
        if(score.get(this.theirs).equals(Integer.valueOf(3))) {
            this.mine = Character.valueOf('B');
        } else if (score.get(this.theirs).equals(Integer.valueOf(1))) {
            this.mine = Character.valueOf('C');
        } else  {
            this.mine = Character.valueOf('A');
        }
    }

    public Character getTheirs() {
        return this.theirs;
    }

    public Character getMine() {
        return this.mine;
    }

    public void setMine(Character mine) {
        this.mine = mine;
    }

    public void setTheirs(Character theirs) {
        this.theirs = theirs;
    }
}


public class Main {
    public static void main(String[] args) {
        String fileName = "/home/mark/src/adventcode/02/input.txt";
        Map<Character, String> name = new HashMap<Character, String>();
        name.put(Character.valueOf('A'), "rock");
        name.put(Character.valueOf('X'), "lose");
        name.put(Character.valueOf('B'), "paper");
        name.put(Character.valueOf('Y'), "tie");
        name.put(Character.valueOf('C'), "scissors");
        name.put(Character.valueOf('Z'), "win");

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
                score += play.getScore().intValue();
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