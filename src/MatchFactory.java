
public class MatchFactory {

    public MatchFactory() {}

    public synchronized void createMatch(Player p1, Player p2) {
        p1.setIsPlaying(true);
        p2.setIsPlaying(true);
        
        Match m = new Match(p1, p2);
        m.start();

        p1.setMatch(m);
        p2.setMatch(m);
    }
}