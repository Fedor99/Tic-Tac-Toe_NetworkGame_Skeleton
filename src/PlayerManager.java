import java.util.List;
import java.util.*;

public class PlayerManager {

    public PlayerManager() {}

    public List<Player> players = new ArrayList<>();
    public List<Player> readyToPlay = new ArrayList<>();
    public List<Integer> freeIDs = new ArrayList<>();

    int findByPlayerID(List<Player> players, int ID) {
        for(int i = 0; i < players.size(); i++)
            if(players.get(i).getID() == ID)
                return i;
        return -1;
    }

    public List<Player> getPlayers() {return players;}
    public List<Player> getReadyToPlayPlayers() { return readyToPlay;}

    public int getFreePlayerID() {
        if(freeIDs.size() > 0) {
            int result = freeIDs.get(0);
            freeIDs.remove(0);
            return result;
        }
        return players.size();
    }
}